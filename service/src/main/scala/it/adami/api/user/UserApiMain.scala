package it.adami.api.user

import java.util.concurrent.Executors

import cats.effect.{ContextShift, ExitCode, IO, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder
import it.adami.api.user.http.routes.{HealthRoutes, VersionRoutes}
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import it.adami.api.user.config.AppConfig
import it.adami.api.user.services.VersionService
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.middleware.Logger
import doobie._
import doobie.implicits._
import cats.effect.IO
import it.adami.api.user.repository.ConnectionBuilder

import scala.concurrent.ExecutionContext

object UserApiMain extends IOApp with LazyLogging {

  def run(args: List[String]): IO[ExitCode] = {

    val versionService = new VersionService

    val routes = Seq(
      new VersionRoutes(versionService)
    ).map(_.routes)
      .reduce(_ <+> _)

    logger.info("Starting service...")

    AppConfig.load flatMap { config =>
      val serviceConfig = config.service
      val postgresConfig = config.postgresConfig

      implicit val executionContext: ExecutionContext =
        ExecutionContext.fromExecutor(Executors.newFixedThreadPool(serviceConfig.threads))

      ConnectionBuilder.generateTransactor(postgresConfig)(contextShift, executionContext)

      val router = Router(
        "" -> (new HealthRoutes).routes,
        s"api/${serviceConfig.apiVersion}" -> routes
      )
      val httpApp = Logger.httpApp(logHeaders = true, logBody = true)(router.orNotFound)

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
