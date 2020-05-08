package it.adami.user.api.test.end

import cats.effect.IO
import io.circe.Json
import it.adami.user.api.test.end.common.JsonBuilder
import org.http4s.{Request, Uri}
import org.http4s.dsl.io.POST
import org.http4s.circe._
import org.http4s.dsl.io._

class UserApiSpec extends SpecBase {

  val notExistingId = 99999 //id that I know doesn't exist

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
        val location = client
          .fetch(postReq) { response =>
            val location = response.headers.toList
              .find(_.name.toString == "Location")
              .get
              .value
              .replace("localhost:8080", serviceHost)
            IO.pure(location)
          }
          .unsafeRunSync()

        val getReq: Request[IO] = Request(uri = Uri.unsafeFromString(location))
        client.status(getReq).map(_.code shouldBe 200).unsafeToFuture

      }
      "return NotFound if the user with the specified id doesn't exist" in {
        val req: Request[IO] = Request(uri = Uri.unsafeFromString(getUserApiPath(notExistingId)))

        client.status(req).map(_.code shouldBe 404).unsafeToFuture
      }
    }

    s"DELETE /api/$apiVersion/users/{id} is called" should {
      "return NoContent if the user exist" in {
        val jsonBody = JsonBuilder.createRequestJson
        val postReq: Request[IO] =
          Request(method = POST, uri = Uri.unsafeFromString(createUserApiPath)).withEntity(jsonBody)
        val id = client
          .fetch(postReq) { response =>
            val location = response.headers.toList.find(_.name.toString == "Location").get.value
            val id = location.substring(location.lastIndexOf("/") + 1).toInt
            IO.pure(id)
          }
          .unsafeRunSync()

        val deleteReq: Request[IO] =
          Request(uri = Uri.unsafeFromString(getDeleteApiPath(id)), method = DELETE)
        client.status(deleteReq).map(_.code shouldBe 204).unsafeToFuture
      }

      "return NotFound if the user doesn't exist" in {
        val deleteReq: Request[IO] =
          Request(uri = Uri.unsafeFromString(getDeleteApiPath(notExistingId)), method = DELETE)
        client.status(deleteReq).map(_.code shouldBe 404).unsafeToFuture

      }
    }

    s"PUT /api/$apiVersion/users/{id} is called" should {

      "return NoContent if the user exist" in {
        val jsonBody = JsonBuilder.createRequestJson
        val postReq: Request[IO] =
          Request(method = POST, uri = Uri.unsafeFromString(createUserApiPath)).withEntity(jsonBody)
        val id = client
          .fetch(postReq) { response =>
            val location = response.headers.toList.find(_.name.toString == "Location").get.value
            val id = location.substring(location.lastIndexOf("/") + 1).toInt
            IO.pure(id)
          }
          .unsafeRunSync()

        val updateReq: Request[IO] =
          Request(uri = Uri.unsafeFromString(getUpdateApiPath(id)), method = PUT)
            .withEntity(JsonBuilder.updateRequestJson)
        client.status(updateReq).map(_.code shouldBe 204).unsafeToFuture
      }

      "return NotFound if the user doesn't exist" in {
        val updateReq: Request[IO] =
          Request(uri = Uri.unsafeFromString(getUpdateApiPath(notExistingId)), method = PUT)
            .withEntity(JsonBuilder.updateRequestJson)
        client.status(updateReq).map(_.code shouldBe 404).unsafeToFuture
      }
    }

  }

}
