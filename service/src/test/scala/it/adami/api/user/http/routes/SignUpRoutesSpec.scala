package it.adami.api.user.http.routes

import it.adami.api.user.SpecBase
import it.adami.api.user.data.UserDataGenerator
import org.http4s.Request
import org.http4s.implicits._
import org.mockito.MockitoSugar
import cats.effect.IO
import it.adami.api.user.services.UserService
import org.http4s.dsl.io._
import io.circe.generic.auto._
import it.adami.api.user.config.ServiceConfig
import it.adami.api.user.errors.UserNameAlreadyInUse
import org.http4s.circe._
import org.scalatest.OptionValues
import io.circe.syntax._

class SignUpRoutesSpec extends SpecBase with MockitoSugar with OptionValues {

  private val userService = mock[UserService]
  private val serviceConfig = ServiceConfig("not-used", 8080, 999, "0.1", "localhost")
  private val registrationRoutes =
    new SignUpRoutes(userService, serviceConfig).routes.orNotFound

  private val createRequest = UserDataGenerator.generateCreateUserRequest

  "RegistrationRoutes" when {

    "POST /signUp is called" should {
      "return Created with a valid request" in {

        when(userService.createUser(createRequest)).thenReturn(IO.pure(Right(123)))
        val response = registrationRoutes
          .run(Request(method = POST, uri = uri"/signUp").withEntity(createRequest.asJson))
          .unsafeRunSync()

        response.status shouldBe Created

        val locationHeaderValue =
          response.headers.toList.find(h => h.name.toString == "Location").value.value

        locationHeaderValue.contains(serviceConfig.externalHost) shouldBe true
        locationHeaderValue.contains(s"api/${serviceConfig.apiVersion}/users") shouldBe true
      }
      "return Conflict when the request email exists already" in {
        when(userService.createUser(createRequest)).thenReturn(IO.pure(Left(UserNameAlreadyInUse)))
        val response = registrationRoutes
          .run(Request(method = POST, uri = uri"/signUp").withEntity(createRequest.asJson))
          .unsafeRunSync()

        response.status shouldBe Conflict

      }

      "return BadRequest if the request is invalid" in {
        val invalidReq = UserDataGenerator.generateCreateUserRequest.copy(gender = "invalid-gender")
        val response = registrationRoutes
          .run(Request(method = POST, uri = uri"/signUp").withEntity(invalidReq.asJson))
          .unsafeRunSync()

        response.status shouldBe BadRequest
      }
    }

  }

}
