package it.adami.bitrock.user.api.test.end

import cats.effect.IO
import com.dimafeng.testcontainers.ForAllTestContainer
import org.http4s.{Request, Status, Uri}

class VersionApiSpec extends SpecBase with ForAllTestContainer {

  "VersionApi" when {
    "GET /api/0.1/version is called" should {
      "return Ok with the version information" in {
        client
          .status(req = Request[IO](uri = Uri.unsafeFromString(versionApiPath)))
          .map(_ shouldBe Status.Ok)
          .unsafeToFuture

      }
    }
  }

}
