package it.adami.user.api.test.end

import cats.effect.IO
import io.circe.Json
import it.adami.api.user.http.json.SearchUsersResponse
import it.adami.user.api.test.end.common.JsonBuilder
import org.http4s.{Request, Uri}
import org.http4s.circe._
import org.http4s.dsl.io._
import io.circe.generic.auto._

class UserApiSpec extends SpecBase {

  private case class SearchUserItem(
      id: Int,
      firstname: String,
      lastname: String,
      email: String
  )

  val notExistingId = 99999 //id that I know doesn't exist

  "UserApi" when {
    s"GET /api/$apiVersion/users/{id} is called" should {
      "return Ok with the JSON detail if the user exist" in {
        val (location, headers) = registerAndActivateUser(JsonBuilder.createRequestJson)
        val getReq: Request[IO] = Request(uri = Uri.unsafeFromString(location)).withHeaders(headers)

        client.status(getReq).map(_.code shouldBe 200).unsafeToFuture
      }
      "return NotFound if the user with the specified id doesn't exist" in {
        val (_, headers) = registerAndActivateUser(JsonBuilder.createRequestJson)
        val req: Request[IO] =
          Request(uri = Uri.unsafeFromString(getUserApiPath(notExistingId))).withHeaders(headers)

        client.status(req).map(_.code shouldBe 404).unsafeToFuture
      }
    }

    s"DELETE /api/$apiVersion/users/{id} is called" should {
      "return NoContent if the user exist" in {
        val (location, headers) = registerAndActivateUser(JsonBuilder.createRequestJson)
        val id = location.substring(location.lastIndexOf("/") + 1).toInt

        val deleteReq: Request[IO] =
          Request(uri = Uri.unsafeFromString(getDeleteApiPath(id)), method = DELETE)
            .withHeaders(headers)
        client.status(deleteReq).map(_.code shouldBe 204).unsafeToFuture
      }

      "return NotFound if the user doesn't exist" in {
        val (_, headers) = registerAndActivateUser(JsonBuilder.createRequestJson)

        val deleteReq: Request[IO] =
          Request(uri = Uri.unsafeFromString(getDeleteApiPath(notExistingId)), method = DELETE)
            .withHeaders(headers)
        client.status(deleteReq).map(_.code shouldBe 404).unsafeToFuture
      }
    }

    s"PUT /api/$apiVersion/users/{id} is called" should {

      "return NoContent if the user exist" in {
        val (location, headers) = registerAndActivateUser(JsonBuilder.createRequestJson)
        val id = location.substring(location.lastIndexOf("/") + 1).toInt

        val updateReq: Request[IO] =
          Request(uri = Uri.unsafeFromString(getUpdateApiPath(id)), method = PUT)
            .withHeaders(headers)
            .withEntity(JsonBuilder.updateRequestJson)
        client.status(updateReq).map(_.code shouldBe 204).unsafeToFuture
      }

      "return NotFound if the user doesn't exist" in {
        val (location, headers) = registerAndActivateUser(JsonBuilder.createRequestJson)

        val updateReq: Request[IO] =
          Request(uri = Uri.unsafeFromString(getUpdateApiPath(notExistingId)), method = PUT)
            .withHeaders(headers)
            .withEntity(JsonBuilder.updateRequestJson)
        client.status(updateReq).map(_.code shouldBe 404).unsafeToFuture
      }
    }

    s"GET /api/$apiVersion/users?search={query} is called" should {
      "return Ok with empty results if no user is found" in {
        val (location, headers) = registerAndActivateUser(JsonBuilder.createRequestJson)

        val req = Request[IO](uri = Uri.unsafeFromString(searchUsersApiPath("invalid_search_query")), method = POST)
          .withHeaders(headers)

        client
          .fetch(req) { response =>
            val json = response.as[Json].unsafeRunSync().hcursor.as[SearchUsersResponse].right.get
            IO(json.items.isEmpty shouldBe true)
          }
          .unsafeToFuture()

      }
    }

  }

}
