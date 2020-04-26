package it.adami.api.user

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder
import it.adami.api.user.http.routes.HealthRoutes
import cats.implicits._
import org.http4s.implicits._

object UserApiMain extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val routes = Seq(
      new HealthRoutes
    ).map(_.routes)
      .reduce(_ <+> _)

    val httpApp = routes.orNotFound

    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }

}
