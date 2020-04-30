package it.adami.api.user.services

import buildinfo.BuildInfo
import it.adami.api.user.SpecBase

class VersionServiceSpec extends SpecBase {

  private val versionService = new VersionService

  "VersionService" should {
    "return the version information inside a json" in {

      val json = versionService.version.hcursor

      json.get[String]("version").map(_ shouldBe BuildInfo.version)
      json.get[String]("name").map(_ shouldBe BuildInfo.name)
      json.get[String]("sbtVersion").map(_ shouldBe BuildInfo.sbtVersion)
      json.get[String]("scalaVersion").map(_ shouldBe BuildInfo.scalaVersion)

    }
  }

}
