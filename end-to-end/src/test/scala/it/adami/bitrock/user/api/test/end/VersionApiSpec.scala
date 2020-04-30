package it.adami.bitrock.user.api.test.end

import cats.effect.IO
import com.dimafeng.testcontainers.ForAllTestContainer
import org.http4s.{Request, Status, Uri}

class VersionApiSpec extends SpecBase with ForAllTestContainer {

  "VersionApi" should "returns 200 status when version endpoint is called" in {
    val basePath = s"http://$serviceHost/api/0.1/version"
    client
      .status(req = Request[IO](uri = Uri.unsafeFromString(basePath)))
      .map(_ shouldBe Status.Ok)
      .unsafeToFuture

  }
}
