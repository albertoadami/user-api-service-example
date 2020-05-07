package it.adami.api.user.http.routes

import cats.effect.IO
import io.circe.Json
import it.adami.api.user.services.UserService
import it.adami.api.user.data.UserDataGenerator
import org.http4s.Request
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import it.adami.api.user.SpecBase
import it.adami.api.user.config.ServiceConfig
import it.adami.api.user.errors.UserNameAlreadyInUse
import org.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, EitherValues, OptionValues}

class UserRoutesSpec
    extends SpecBase
    with MockitoSugar
    with OptionValues
    with EitherValues
    with BeforeAndAfterEach {

  private val request = UserDataGenerator.generateCreateUserRequest

  private val userService = mock[UserService]
  private val serviceConfig = ServiceConfig("not-used", 8080, 999, "0.1", "localhost")
  private val userRoutes = new UserRoutes(userService, serviceConfig).routes.orNotFound

  "UserRoutes" should {
    "return Created when a valid user creation request is provided" in {

      when(userService.createUser(request)).thenReturn(IO.pure(Right(123)))
      val response = userRoutes
        .run(Request(method = POST, uri = uri"/users").withEntity(request.asJson))
        .unsafeRunSync()

      response.status shouldBe Created

      val locationHeaderValue =
        response.headers.toList.find(h => h.name.toString == "Location").value.value

      locationHeaderValue.contains(serviceConfig.externalHost) shouldBe true
      locationHeaderValue.contains(s"api/${serviceConfig.apiVersion}/users") shouldBe true
    }

    "return Conflict when the user creation request contains a username already in use" in {
      when(userService.createUser(request)).thenReturn(IO.pure(Left(UserNameAlreadyInUse)))
      val response = userRoutes
        .run(Request(method = POST, uri = uri"/users").withEntity(request.asJson))
        .unsafeRunSync()

      response.status shouldBe Conflict

    }

    "return BadRequest if the user creation request is invalid" in {
      val invalidReq = UserDataGenerator.generateCreateUserRequest.copy(gender = "invalid-gender")
      val response = userRoutes
        .run(Request(method = POST, uri = uri"/users").withEntity(invalidReq.asJson))
        .unsafeRunSync()

      response.status shouldBe BadRequest
    }

    "return NotFound if the requested user not exist" in {
      when(userService.findUser(999)).thenReturn(IO.pure(None))
      val response = userRoutes
        .run(Request(uri = uri"/users/999"))
        .unsafeRunSync()

      response.status shouldBe NotFound
    }

    "return Ok with the json if the user exist" in {
      val userGenerated = UserDataGenerator.generateUserDetailResponse
      when(userService.findUser(999))
        .thenReturn(IO.pure(Some(userGenerated)))
      val response = userRoutes
        .run(Request(uri = uri"/users/999"))
        .unsafeRunSync()

      val hcursor = response.as[Json].unsafeRunSync.hcursor
      hcursor.get[String]("firstname").right.value shouldBe userGenerated.firstname
      hcursor.get[String]("lastname").right.value shouldBe userGenerated.lastname
      hcursor.get[String]("email").right.value shouldBe userGenerated.email
      hcursor.get[String]("gender").right.value shouldBe userGenerated.gender
      hcursor.get[String]("dateOfBirth").right.value shouldBe userGenerated.dateOfBirth
    }

    "return NoContent with a delete user with invalid id request" in {}
  }

}
