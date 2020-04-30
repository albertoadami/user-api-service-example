package it.adami.api.user.http.routes

import cats.effect.IO
import it.adami.api.user.services.VersionService
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.circe._

class VersionRoutes(versionService: VersionService) extends BaseRoutes {
  override val routes: HttpRoutes[IO] = HttpRoutes
    .of[IO] {
      case GET -> Root / "version" =>
        Ok(versionService.version)
    }
}
