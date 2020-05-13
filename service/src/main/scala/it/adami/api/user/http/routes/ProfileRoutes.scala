package it.adami.api.user.http.routes
import cats.effect.IO
import it.adami.api.user.http.authentication.UserInfo
import it.adami.api.user.services.UserService
import org.http4s.dsl.io.{/, Root}
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.server.AuthMiddleware
import org.http4s.dsl.io._

/**
  * Contains all the routes for the profile management(activation, change password, ecc..)
  */
class ProfileRoutes(userService: UserService, authMiddleware: AuthMiddleware[IO, UserInfo]) extends BaseRoutes {

  private val authedRoutes: AuthedRoutes[UserInfo, IO] = AuthedRoutes.of {
    case POST -> Root / "profile" / "activate" as user =>
      userService.activateUser(user.id).flatMap(_ => NoContent())

  }

  override def routes: HttpRoutes[IO] = authMiddleware(authedRoutes)

}
