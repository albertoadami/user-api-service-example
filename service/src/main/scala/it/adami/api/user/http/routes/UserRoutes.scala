package it.adami.api.user.http.routes

import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import it.adami.api.user.http.json.{CreateUserRequest, ErrorsResponse}
import org.http4s.HttpRoutes
import it.adami.api.user.services.UserService
import org.http4s.dsl.io._
import org.http4s.circe.CirceEntityEncoder._
import it.adami.api.user.errors.UserNameAlreadyInUse
import io.circe.generic.auto._
import it.adami.api.user.validation.user.CreateUserValidation
import org.http4s.circe._

class UserRoutes(userService: UserService) extends BaseRoutes with LazyLogging {

  private def handleCreateUserResponses(req: CreateUserRequest) =
    userService.createUser(req).flatMap {
      case Right(id) =>
        Created(id)
      case Left(UserNameAlreadyInUse) =>
        Conflict()
    }

  override val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "users" =>
      for {
        json <- req.decodeJson[CreateUserRequest]
        validated = CreateUserValidation(json)
        response <- validated.fold(
          errors => BadRequest(getErrorsResponse(errors)),
          valid => handleCreateUserResponses(valid)
        )
      } yield response

  }

}
