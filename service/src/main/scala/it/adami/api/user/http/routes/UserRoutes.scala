package it.adami.api.user.http.routes

import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import it.adami.api.user.http.json.{CreateUserRequest, UpdateUserRequest}
import org.http4s.{AuthedRoutes, HttpRoutes, Uri}
import it.adami.api.user.services.UserService
import org.http4s.dsl.io._
import org.http4s.circe.CirceEntityEncoder._
import it.adami.api.user.errors.{UserNameAlreadyInUse, UserNotFound}
import it.adami.api.user.validation.user.{CreateUserValidation, UpdateUserValidation}
import org.http4s.circe._
import io.circe.generic.auto._
import it.adami.api.user.config.ServiceConfig
import it.adami.api.user.http.authentication.UserInfo
import org.http4s.headers.Location
import org.http4s.server.AuthMiddleware

class UserRoutes(
    userService: UserService,
    serviceConfig: ServiceConfig,
    authMiddleware: AuthMiddleware[IO, UserInfo]
) extends BaseRoutes
    with LazyLogging {

  private def handleCreateUserResponses(req: CreateUserRequest) = {
    //TODO maybe is better to move the location builder in a separate class
    def generateLocationForUser(id: Int): Uri =
      Uri.unsafeFromString(
        s"http://${serviceConfig.externalHost}/api/${serviceConfig.apiVersion}/users/$id"
      )
    userService.createUser(req).flatMap {
      case Right(id) =>
        Created(Location(generateLocationForUser(id)))
      case Left(UserNameAlreadyInUse) =>
        Conflict()
    }
  }

  private def handleUpdateUserResponse(id: Int, req: UpdateUserRequest) = {
    userService.updateUser(id, req).flatMap {
      case Right(_) =>
        NoContent()
      case Left(UserNotFound) =>
        NotFound()
    }
  }

  val authedRoutes: AuthedRoutes[UserInfo, IO] = AuthedRoutes.of {
    case req @ POST -> Root / "users" as user =>
      for {
        json <- req.req.decodeJson[CreateUserRequest]
        validated = CreateUserValidation(json)
        response <- validated.fold(
          errors => BadRequest(getErrorsResponse(errors)),
          valid => handleCreateUserResponses(valid)
        )
      } yield response
    case GET -> Root / "users" / IntVar(userId) as user =>
      for {
        result <- userService.findUser(userId)
        response <- result.fold(NotFound())(value => Ok(value))
      } yield response
    case DELETE -> Root / "users" / IntVar(userId) as user =>
      for {
        result <- userService.deleteUser(userId)
        response <- result.fold(_ => NotFound(), _ => NoContent())
      } yield response
    case req @ PUT -> Root / "users" / IntVar(userId) as user =>
      for {
        json <- req.req.decodeJson[UpdateUserRequest]
        validated = UpdateUserValidation(json)
        response <- validated.fold(
          errors => BadRequest(getErrorsResponse(errors)),
          valid => handleUpdateUserResponse(userId, valid)
        )
      } yield response

  }

  override val routes: HttpRoutes[IO] = authMiddleware(authedRoutes)

}
