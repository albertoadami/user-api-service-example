package it.adami.api.user

import java.util.concurrent.Executors

import cats.effect.{ExitCode, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder
import it.adami.api.user.http.routes.{HealthRoutes, UserRoutes, VersionRoutes}
import com.typesafe.scalalogging.LazyLogging
import it.adami.api.user.config.AppConfig
import it.adami.api.user.services.{UserService, VersionService}
import cats.effect.IO
import it.adami.api.user.http.RoutesBuilder
import it.adami.api.user.repository.{DatabaseManager, UserRepository}

import scala.concurrent.ExecutionContext

object UserApiMain extends IOApp with LazyLogging {

  def run(args: List[String]): IO[ExitCode] = {

    logger.info("Starting service...")

    AppConfig.load flatMap { config =>
      val serviceConfig = config.service
      val postgresConfig = config.postgres

      implicit val executionContext: ExecutionContext =
        ExecutionContext.fromExecutor(Executors.newFixedThreadPool(serviceConfig.threads))

      DatabaseManager.generateTransactor(postgresConfig)(contextShift, executionContext).use { xa =>
        val userRepository = UserRepository(xa)

        val userService = new UserService(userRepository)
        val versionService = new VersionService

        val routes = Seq(
          new VersionRoutes(versionService),
          new UserRoutes(userService)
        )

        val routesBuilder = new RoutesBuilder(routes, new HealthRoutes, serviceConfig)
        val httpApp = routesBuilder.buildApp

        DatabaseManager.initialize(xa).flatMap { _ =>
          BlazeServerBuilder[IO](executionContext)
            .withExecutionContext(executionContext)
            .bindHttp(serviceConfig.port, serviceConfig.host)
            .withHttpApp(httpApp)
            .resource
            .use(_ => IO.never)
            .as(ExitCode.Success)
        }

      }

    }
  }

}
