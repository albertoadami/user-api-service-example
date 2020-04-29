package it.adami.api.user

import java.util.concurrent.Executors

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder
import it.adami.api.user.http.routes.HealthRoutes
import cats.implicits._
import org.http4s.implicits._

import scala.concurrent.ExecutionContext

object UserApiMain extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val routes = Seq(
      new HealthRoutes
    ).map(_.routes)
      .reduce(_ <+> _)

    val httpApp = routes.orNotFound

    val executionContext: ExecutionContext =
      ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

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
