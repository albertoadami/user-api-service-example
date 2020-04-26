package it.adami.api.user

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder
import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.syntax._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._

object UserApiMain extends IOApp {

  val healthService = HttpRoutes
    .of[IO] {
      case GET -> Root / "health" =>
        NoContent()
    }
    .orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(healthService)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

}
