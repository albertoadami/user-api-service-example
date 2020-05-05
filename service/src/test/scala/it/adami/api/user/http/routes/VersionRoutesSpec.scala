package it.adami.api.user.http.routes

import io.circe.Json
import it.adami.api.user.services.VersionService
import org.http4s.{Request, Status}
import org.mockito.MockitoSugar
import it.adami.api.user.SpecBase
import org.http4s.circe._
import org.http4s.implicits._
import org.scalatest.EitherValues

class VersionRoutesSpec extends SpecBase with MockitoSugar with EitherValues {

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
      val hcursor = response.as[Json].unsafeRunSync().hcursor

      hcursor.get[String]("name").right.value shouldBe "user-api"
      hcursor.get[String]("version").right.value shouldBe "some-version"
      hcursor.get[String]("scalaVersion").right.value shouldBe "scalaVersion"
      hcursor.get[String]("sbtVersion").right.value shouldBe "my-sbt-version"

    }
  }

}
