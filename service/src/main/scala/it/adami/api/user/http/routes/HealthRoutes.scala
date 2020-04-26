package it.adami.api.user.http.routes

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

class HealthRoutes extends BaseRoutes {

  override val routes: HttpRoutes[IO] =
    HttpRoutes
      .of[IO] {
        case GET -> Root / "health" =>
          NoContent()
      }

}
