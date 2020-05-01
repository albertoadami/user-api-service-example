package it.adami.api.user.http.routes

import io.circe.Json
import it.adami.api.user.SpecBase
import it.adami.api.user.services.VersionService
import org.http4s.{Request, Status}
import org.mockito.MockitoSugar
import org.http4s.circe._
import org.http4s.implicits._

class VersionRoutesSpec extends SpecBase with MockitoSugar {

  private val mockVersionResponse =
    Json.obj(
      "name" -> Json.fromString("user-api"),
      "version" -> Json.fromString("some-version"),
      "scalaVersion" -> Json.fromString("scalaVersion"),
      "sbtVersion" -> Json.fromString("my-sbt-version")
    )


  private val versionService = mock[VersionService]
  when(versionService.version).thenReturn(mockVersionResponse)
  private val versionRoutes = new VersionRoutes(versionService).routes.orNotFound

  "VersionRoutes" should {
    "return Ok when the version endpoint is called" in {
      val response = versionRoutes.run(Request(uri = uri"/version")).unsafeRunSync()

      response.status shouldBe Status.Ok
      response.as[Json] map { json =>
        val hcursor = json.hcursor

        hcursor.get[String]("name").map(_ shouldBe "user-api")
        hcursor.get[String]("version").map(_ shouldBe "some-version")
        hcursor.get[String]("scalaVersion").map(_ shouldBe "scalaVersion")
        hcursor.get[String]("sbtVersion").map(_ shouldBe "my-sbt-version")

      }
    }
  }

}
