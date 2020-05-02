package it.adami.user.api.test.end

import cats.effect.IO
import com.dimafeng.testcontainers.ForAllTestContainer
import it.adami.user.api.test.end.common.JsonBuilder
import org.http4s.{Request, Uri}
import org.http4s.dsl.io.POST
import org.http4s.circe._

class UserApiSpec extends SpecBase with ForAllTestContainer {

  "UserApi" when {
    "POST /api/0.1/users is called" should {
      "return Created for a valid json request" in {
        val jsonBody = JsonBuilder.createRequestJson
        val req: Request[IO] =
          Request(method = POST, uri = Uri.unsafeFromString(createUserApiPath)).withEntity(jsonBody)
        client.status(req).map(value => value.code shouldBe 201).unsafeToFuture
      }
    }
  }

}
