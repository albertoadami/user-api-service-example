package it.adami.api.user.http

import cats.effect.{ContextShift, IO}
import it.adami.api.user.config.ServiceConfig
import it.adami.api.user.http.routes.{BaseRoutes, HealthRoutes}
import org.http4s.HttpApp
import org.http4s.server.Router
import org.http4s.server.middleware.Logger
import org.http4s.implicits._
import cats.implicits._

class RoutesBuilder(
    routes: Seq[BaseRoutes],
    healthRoutes: HealthRoutes,
    serviceConfig: ServiceConfig
) {

  private val allRoutes = routes
    .map(_.routes)
    .reduce(_ <+> _)

  def buildApp(implicit contextShift: ContextShift[IO]): HttpApp[IO] = {
    val router = Router(
      "" -> healthRoutes.routes,
      s"api/${serviceConfig.apiVersion}" -> allRoutes
    )
    Logger.httpApp(logHeaders = true, logBody = true)(router.orNotFound)
  }

}
