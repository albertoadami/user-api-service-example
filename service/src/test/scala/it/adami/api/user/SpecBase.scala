package it.adami.api.user

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import it.adami.api.user.http.authentication.UserInfo
import it.adami.api.user.http.json.ErrorsResponse
import org.http4s.{AuthedRoutes, Request}
import org.http4s.dsl.io._
import org.http4s.server.AuthMiddleware
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityEncoder._

trait SpecBase extends AnyWordSpec with Matchers {
  def mockAuthMiddleWare: AuthMiddleware[IO, UserInfo] = {
    val onFailure: AuthedRoutes[ErrorsResponse, IO] = Kleisli { req =>
      OptionT.liftF(Forbidden(req.context))
    }
    val authUser: Kleisli[IO, Request[IO], Either[ErrorsResponse, UserInfo]] = Kleisli({ _ =>
      IO.pure(Right(UserInfo("test@test.it", enabled = true)))
    })
    AuthMiddleware(authUser, onFailure)
  }
}
