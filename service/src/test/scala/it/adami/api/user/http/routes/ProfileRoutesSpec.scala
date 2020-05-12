package it.adami.api.user.http.routes

import cats.effect.IO
import it.adami.api.user.SpecBase
import it.adami.api.user.services.UserService
import org.http4s.Request
import org.mockito.MockitoSugar
import org.mockito.ArgumentMatchersSugar
import org.http4s.implicits._
import org.http4s.dsl.io._

class ProfileRoutesSpec extends SpecBase with MockitoSugar with ArgumentMatchersSugar {

  private val userService = mock[UserService]
  private val accountRoutes = new ProfileRoutes(userService, mockAuthMiddleWare).routes.orNotFound

  "AccountRoutes" when {
    "POST /profile/activate is called" should {
      "return NoContent response" in {
        when(userService.activateUser(anyInt)).thenReturn(IO.pure(()))
        val response = accountRoutes.run(Request(method = POST, uri = uri"/profile/activate")).unsafeRunSync()

        response.status shouldBe NoContent

      }
    }
  }

}
