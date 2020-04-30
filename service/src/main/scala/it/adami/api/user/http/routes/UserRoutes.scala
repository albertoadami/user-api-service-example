package it.adami.api.user.http.routes

import cats.effect.IO
import it.adami.api.user.http.json.CreateUserRequest
import org.http4s.HttpRoutes
import it.adami.api.user.services.UserService
import org.http4s.dsl.io._
import org.http4s.circe.CirceEntityEncoder._
import it.adami.api.user.errors.UserNameAlreadyInUse

class UserRoutes(userService: UserService) extends BaseRoutes {

  override val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "users" =>
      val json = req.as[CreateUserRequest]
      json.flatMap(userService.createUser).flatMap {
        case Right(id) =>
          Created(id)
        case Left(UserNameAlreadyInUse) =>
          Conflict()
      }

  }

}
