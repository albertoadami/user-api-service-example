package it.adami.api.user.http.routes

import it.adami.api.user.SpecBase
import it.adami.api.user.services.VersionService
import org.http4s.{Request, Status}
import org.http4s.implicits._

class VersionRoutesSpec extends SpecBase {

  private val versionService = new VersionService
  private val versionRoutes = new VersionRoutes(versionService).routes.orNotFound

  "VersionRoutes" should {
    "return Ok when the version endpoint is called" in {
      val response = versionRoutes.run(Request(uri = uri"/version")).unsafeRunSync()

      response.status shouldBe Status.Ok
    }
  }

}
