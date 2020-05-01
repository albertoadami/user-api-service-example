package it.adami.bitrock.user.api.test.end

import cats.effect.IO
import com.dimafeng.testcontainers.ForAllTestContainer
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpecLike

trait SpecBase extends AsyncWordSpecLike with Matchers with UserApiContainer { this: ForAllTestContainer =>

  val client: Client[IO] = ClientBuilder.createClient

}
