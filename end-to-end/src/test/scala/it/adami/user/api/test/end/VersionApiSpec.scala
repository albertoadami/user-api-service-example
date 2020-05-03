package it.adami.user.api.test.end

import buildinfo.BuildInfo
import io.circe.Json
import org.http4s.Uri
import org.http4s.circe._

class VersionApiSpec extends SpecBase {

  "VersionApi" when {
    "GET /api/0.1/version is called" should {
      "return Ok with the version information" in {
        client
          .expect[Json](uri = Uri.unsafeFromString(versionApiPath))
          .map { json =>
            val hcursor = json.hcursor

            hcursor.get[String]("version").right.get shouldBe BuildInfo.version
            hcursor.get[String]("name").right.get shouldBe "user-api"
            hcursor.get[String]("sbtVersion").right.get shouldBe BuildInfo.sbtVersion
            hcursor.get[String]("scalaVersion").right.get shouldBe BuildInfo.scalaVersion
          }
          .unsafeToFuture
      }
    }
  }

}
