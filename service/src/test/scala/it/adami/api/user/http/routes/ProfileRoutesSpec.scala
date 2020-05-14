package it.adami.api.user.http.routes

import cats.effect.IO
import io.circe.Json
import it.adami.api.user.SpecBase
import it.adami.api.user.services.ProfileService
import org.http4s.Request
import org.mockito.MockitoSugar
import org.mockito.ArgumentMatchersSugar
import org.http4s.dsl.io._
import it.adami.api.user.errors.WrongOldPasswordError
import org.http4s.implicits._
import org.http4s.circe._

import scala.util.Random

class ProfileRoutesSpec extends SpecBase with MockitoSugar with ArgumentMatchersSugar {

  private val profileService = mock[ProfileService]
  private val accountRoutes = new ProfileRoutes(profileService, mockAuthMiddleWare).routes.orNotFound

  "AccountRoutes" when {
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
