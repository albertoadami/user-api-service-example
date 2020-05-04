package it.adami.user.api.test.end

import cats.effect.IO
import io.circe.Json
import it.adami.user.api.test.end.common.JsonBuilder
import org.http4s.{Request, Uri}
import org.http4s.dsl.io.POST
import org.http4s.circe._

import scala.util.Random

class UserApiSpec extends SpecBase {

  "UserApi" when {
    s"POST /api/$apiVersion/users is called" should {
      "return Created for a valid json request" in {
        val jsonBody = JsonBuilder.createRequestJson
        val req: Request[IO] =
          Request(method = POST, uri = Uri.unsafeFromString(createUserApiPath)).withEntity(jsonBody)
        client.status(req).map(value => value.code shouldBe 201).unsafeToFuture
      }

      "return UnProcessableEntity with a bad json request" in {
        val jsonBody = Json.obj("test" -> Json.fromString("test"))
        val req: Request[IO] =
          Request(method = POST, uri = Uri.unsafeFromString(createUserApiPath)).withEntity(jsonBody)
        client.status(req).map(value => value.code shouldBe 422).unsafeToFuture
      }

      "return Conflict if exist already a user with the same email" in {
        val jsonBody = JsonBuilder.createRequestJson
        val req: Request[IO] =
          Request(method = POST, uri = Uri.unsafeFromString(createUserApiPath)).withEntity(jsonBody)
        client.status(req).unsafeRunSync() //create the user

        client.status(req).map(value => value.code shouldBe 409).unsafeToFuture

      }
    }

    s"GET /api/$apiVersion/users/{id} is called" should {
      "return Ok with the JSON detail if the user exist" in {
        val jsonBody = JsonBuilder.createRequestJson
        val postReq: Request[IO] =
          Request(method = POST, uri = Uri.unsafeFromString(createUserApiPath)).withEntity(jsonBody)
        val location = client.fetch(postReq){response =>
          val location = response.headers.toList.find(_.name.toString == "Location").get.value.replace("localhost:8080", serviceHost)
          IO.pure(location)
        }.unsafeRunSync()

        val getReq: Request[IO] = Request(uri = Uri.unsafeFromString(location))
        client.status(getReq).map(_.code shouldBe 200).unsafeToFuture

      }
      "return NotFound if the user with the specified id doesn't exist" in {
        val unExistingId = Random.nextInt(6)
        val req: Request[IO] = Request(uri = Uri.unsafeFromString(getUserApiPath(unExistingId)))

        client.status(req).map(_.code shouldBe 200).unsafeToFuture
      }
    }

  }

}
