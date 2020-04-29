package it.adami.api.user

import java.util.concurrent.Executors

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder
import it.adami.api.user.http.routes.HealthRoutes
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext

object UserApiMain extends IOApp with LazyLogging {

  def run(args: List[String]): IO[ExitCode] = {
    val routes = Seq(
      new HealthRoutes
    ).map(_.routes)
      .reduce(_ <+> _)

    val httpApp = Logger.httpApp(logHeaders = true, logBody = true)(routes.orNotFound)

    val executionContext: ExecutionContext =
      ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

    logger.info("Starting service...")

    BlazeServerBuilder[IO]
      .withExecutionContext(executionContext)
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }

}
