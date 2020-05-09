package it.adami.api.user.http.authentication

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import it.adami.api.user.http.json.{ErrorItem, ErrorsResponse}
import it.adami.api.user.repository.UserRepository
import org.http4s.{AuthedRoutes, BasicCredentials, Request, headers}
import org.http4s.dsl.io._
import org.http4s.server.AuthMiddleware
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityEncoder._

class Authentication(userRepository: UserRepository) extends LazyLogging {

  private val onFailure: AuthedRoutes[ErrorsResponse, IO] = Kleisli { result =>
    logger.error(s"authentication error ${result.context} during request ${result.req}")
    OptionT.liftF(Forbidden(result.context))
  }

  private def checkCredentials(
      email: String,
      password: String
  ): IO[Either[ErrorsResponse, UserInfo]] =
    userRepository.findUserByEmail(email) map {
      case Some(user) =>
        if (user.password == password) Right(UserInfo(email = email, enabled = user.enabled))
        else Left(ErrorsResponse(List(ErrorItem(errorDescription = "password is not correct"))))
      case None =>
        Left(
          ErrorsResponse(List(ErrorItem(errorDescription = s"user with email $email not found")))
        )
    }

  private val authUser
      : Kleisli[IO, Request[IO], Either[ErrorsResponse, UserInfo]] = Kleisli({ request =>
    request.headers
      .get(headers.Authorization)
      .map { authHeader =>
        val credentials = BasicCredentials(authHeader.value.split(" ").toList.last)
        checkCredentials(credentials.username, credentials.password)
          .map(_.fold(errors => Left(errors), user => Right(user)))
      }
      .getOrElse(
        IO(
          Left(
            ErrorsResponse(List(ErrorItem(errorDescription = "Authorization header not provided")))
          )
        )
      )
  })

  /**
    * method that generate the middleware used by the Routes
    * @return the middleware for the security check
    */
  def middleware: AuthMiddleware[IO, UserInfo] = AuthMiddleware(authUser, onFailure)

}
