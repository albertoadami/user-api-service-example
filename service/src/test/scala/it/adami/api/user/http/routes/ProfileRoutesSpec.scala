package it.adami.api.user.http.routes

import cats.effect.IO
import io.circe.Json
import it.adami.api.user.SpecBase
import it.adami.api.user.data.UserDataGenerator
import it.adami.api.user.services.ProfileService
import org.http4s.Request
import org.mockito.MockitoSugar
import org.mockito.ArgumentMatchersSugar
import org.http4s.dsl.io._
import it.adami.api.user.errors.WrongOldPasswordError
import org.http4s.implicits._
import org.http4s.circe._
import org.scalatest.{EitherValues, OptionValues}

import scala.util.Random

class ProfileRoutesSpec extends SpecBase with MockitoSugar with ArgumentMatchersSugar with EitherValues {

  private val profileService = mock[ProfileService]
  private val accountRoutes = new ProfileRoutes(profileService, mockAuthMiddleWare).routes.orNotFound

  "AccountRoutes" when {
    "GET /profile is called" should {
      "return the logged user information" in {
        val mockedResponse = UserDataGenerator.generateUserProfileResponse
        when(profileService.getProfile(anyInt)).thenReturn(IO.pure(mockedResponse))

        val response = accountRoutes.run(Request(method = GET, uri = uri"/profile")).unsafeRunSync()

        response.status shouldBe Ok
        val hcursor = response.as[Json].unsafeRunSync.hcursor

        hcursor.get[String]("firstname").right.value shouldBe mockedResponse.firstname
        hcursor.get[String]("lastname").right.value shouldBe mockedResponse.lastname
        hcursor.get[String]("email").right.value shouldBe mockedResponse.email
        hcursor.get[String]("gender").right.value shouldBe mockedResponse.gender
        hcursor.get[String]("dateOfBirth").right.value shouldBe mockedResponse.dateOfBirth

      }
    }

    "POST /profile/activate is called" should {
      "return NoContent response" in {
        when(profileService.activateUser(anyInt)).thenReturn(IO.pure(()))
        val response = accountRoutes.run(Request(method = POST, uri = uri"/profile/activate")).unsafeRunSync()

        response.status shouldBe NoContent

      }
    }

    "PUT /profile/password is called" should {
      "return BadRequest if one of the passwords provided are invalid" in {
        val wrongJson = Json.obj("oldPassword" -> Json.fromString("a"), "newPassword" -> Json.fromString("b"))
        val req = Request[IO](method = PUT, uri = uri"/profile/password").withEntity(wrongJson)
        accountRoutes.run(req).unsafeRunSync().status shouldBe BadRequest
      }
      "return BadRequest if the old password is wrong" in {
        when(profileService.changePassword(anyInt, any[String], any[String]))
          .thenAnswer(IO.pure(Left(WrongOldPasswordError)))
        val json = Json.obj(
          "oldPassword" -> Json.fromString(Random.nextString(8)),
          "newPassword" -> Json.fromString(Random.nextString(8))
        )
        val req = Request[IO](method = PUT, uri = uri"/profile/password").withEntity(json)
        accountRoutes.run(req).unsafeRunSync().status shouldBe BadRequest
      }

      "return NoContent if the old password is correct" in {
        when(profileService.changePassword(anyInt, any[String], any[String])).thenAnswer(IO.pure(Right()))
        val json = Json.obj(
          "oldPassword" -> Json.fromString(Random.nextString(8)),
          "newPassword" -> Json.fromString(Random.nextString(8))
        )
        val req = Request[IO](method = PUT, uri = uri"/profile/password").withEntity(json)
        accountRoutes.run(req).unsafeRunSync().status shouldBe NoContent

      }
    }
  }

}
