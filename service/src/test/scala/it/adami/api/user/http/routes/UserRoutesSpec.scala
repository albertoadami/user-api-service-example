package it.adami.api.user.http.routes

import cats.effect.IO
import it.adami.api.user.services.UserService
import it.adami.api.user.data.UserDataGenerator
import org.http4s.Request
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import it.adami.api.user.SpecBase
import it.adami.api.user.errors.UserNameAlreadyInUse
import org.mockito.MockitoSugar

class UserRoutesSpec extends SpecBase with MockitoSugar {

  private val request = UserDataGenerator.generateCreateUserRequest

  private val userService = mock[UserService]
  private val userRoutes = new UserRoutes(userService).routes.orNotFound

  "UserRoutes" should {
    "return Created when a valid user creation request is provided" in {

      when(userService.createUser(request)).thenReturn(IO.pure(Right("123")))
      val response = userRoutes
        .run(Request(method = POST, uri = uri"/users").withEntity(request.asJson))
        .unsafeRunSync()

      response.status shouldBe Created
    }

    "return Conflict when the user creation request contains a username already in use" in {
      when(userService.createUser(request)).thenReturn(IO.pure(Left(UserNameAlreadyInUse)))
      val response = userRoutes
        .run(Request(method = POST, uri = uri"/users").withEntity(request.asJson))
        .unsafeRunSync()

      response.status shouldBe Conflict

    }

    "return BadRequest if the json request is invalid" in {
      val invalidReq = UserDataGenerator.generateCreateUserRequest.copy(gender = "invalid-gender")
      val response = userRoutes
        .run(Request(method = POST, uri = uri"/users").withEntity(invalidReq.asJson))
        .unsafeRunSync()

      response.status shouldBe BadRequest
    }

  }

}
