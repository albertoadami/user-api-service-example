package it.adami.api.user.http.routes
import cats.effect.IO
import it.adami.api.user.http.authentication.UserInfo
import it.adami.api.user.http.json.{ChangePasswordRequest, ErrorItem, ErrorsResponse}
import it.adami.api.user.services.ProfileService
import org.http4s.dsl.io.{/, Root}
import org.http4s.{AuthedRoutes, HttpRoutes, Response}
import org.http4s.server.AuthMiddleware
import org.http4s.dsl.io._
import org.http4s.circe.CirceEntityEncoder._
import io.circe.generic.auto._
import it.adami.api.user.errors.WrongOldPasswordError
import org.http4s.circe._
import it.adami.api.user.validation.user.ChangePasswordValidation

/**
  * Contains all the routes for the profile management(activation, change password, ecc..)
  */
class ProfileRoutes(profileService: ProfileService, authMiddleware: AuthMiddleware[IO, UserInfo]) extends BaseRoutes {

  private def handleUpdatePasswordResponse(id: Int, req: ChangePasswordRequest): IO[Response[IO]] =
    profileService.changePassword(id, req.oldPassword, req.newPassword) flatMap {
      case Right(_) =>
        NoContent()
      case Left(WrongOldPasswordError) =>
        BadRequest(
          ErrorsResponse(ErrorItem(field = Some("oldPassword"), errorDescription = "The old password value is wrong"))
        )
    }

  private val authedRoutes: AuthedRoutes[UserInfo, IO] = AuthedRoutes.of {
    case GET -> Root / "profile" as user =>
      profileService.getProfile(user.id).flatMap(Ok(_))
    case POST -> Root / "profile" / "activate" as user =>
      profileService.activateUser(user.id).flatMap(_ => NoContent())
    case req @ PUT -> Root / "profile" / "password" as user =>
      for {
        json <- req.req.decodeJson[ChangePasswordRequest]
        validated = ChangePasswordValidation(json)
        response <- validated.fold(
          errors => BadRequest(getErrorsResponse(errors)),
          valid => handleUpdatePasswordResponse(user.id, valid)
        )
      } yield response

  }

  override def routes: HttpRoutes[IO] = authMiddleware(authedRoutes)

}
