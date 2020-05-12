package it.adami.user.api.test.end

import it.adami.user.api.test.end.common.JsonBuilder

class ProfileApiSpec extends SpecBase {
  "ProfileApi" when {
    "POST /profile/activate is called" should {
      "return NoContent if the credentials are correct" in {
        val (_, headers) = registerUser(JsonBuilder.createRequestJson)
        activateUser(headers).code shouldBe 204
      }
    }
  }
}
