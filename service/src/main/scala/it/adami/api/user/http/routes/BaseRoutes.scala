package it.adami.api.user.http.routes

import cats.effect.IO
import org.http4s.HttpRoutes

trait BaseRoutes {

  def routes: HttpRoutes[IO]

}
