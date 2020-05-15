package it.adami.user.api.test.end

import cats.effect.IO
import io.circe.Json
import it.adami.user.api.test.end.common.JsonBuilder
import org.http4s.{Request, Uri}
import org.http4s.dsl.io._
import org.http4s.circe._

import scala.util.Random

class ProfileApiSpec extends SpecBase {
  "ProfileApi" when {
    "GET /profile is called" should {
      "return Ok with the logged user info" in {
        val (_, headers) = registerUser(JsonBuilder.createRequestJson)
        client
          .status(Request[IO](uri = Uri.unsafeFromString(profileApiPath)).withHeaders(headers))
          .map(_.code shouldBe 200)
          .unsafeToFuture

      }
    }

    "POST /profile/activate is called" should {
      "return NoContent if the credentials are correct" in {
        val (_, headers) = registerUser(JsonBuilder.createRequestJson)
        activateUser(headers).code shouldBe 204
      }
    }
    "PUT /profile/password is called" should {
      "return NoContent if the old and new passwords are correct" in {
        val createUserReq = JsonBuilder.createRequestJson
        val password = createUserReq.hcursor.get[String]("password").right.get
        val (_, headers) = registerUser(createUserReq)
        val changePwdJson = Json.obj(
          "oldPassword" -> Json.fromString(password),
          "newPassword" -> Json.fromString(s"testtest${Random.nextInt(5)}")
        )

        val req = Request[IO](method = PUT, uri = Uri.unsafeFromString(changePasswordApiPath))
          .withHeaders(headers)
          .withEntity(changePwdJson)
        client
          .status(req)
          .map(_.code shouldBe 204)
          .unsafeToFuture
      }

      "return BadRequest if the old password is not correct" in {
        val createUserReq = JsonBuilder.createRequestJson
        val password = createUserReq.hcursor.get[String]("password").right.get
        val (_, headers) = registerUser(createUserReq)
        val changePwdJson = Json.obj(
          "oldPassword" -> Json.fromString(Random.nextString(15)),
          "newPassword" -> Json.fromString(s"testtest${Random.nextInt(5)}")
        )

        val req = Request[IO](method = PUT, uri = Uri.unsafeFromString(changePasswordApiPath))
          .withHeaders(headers)
          .withEntity(changePwdJson)
        client
          .status(req)
          .map(_.code shouldBe 400)
          .unsafeToFuture
      }
    }
  }
}
