package it.adami.api.user.http.routes

import org.http4s.{Request, Status}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.http4s.implicits._

class HealthRoutesSpec extends AnyWordSpec with Matchers {

  private val healthRoutes = (new HealthRoutes).routes.orNotFound

  "HealthRoutes" should {
    "return NoContent when health endpoint is called" in {
      val response = healthRoutes.run(Request(uri = uri"/health")).unsafeRunSync()

      response.status shouldBe Status.NoContent

    }
  }

}
