package it.adami.api.user.services

import io.circe._
import io.circe.literal._
import buildinfo.BuildInfo

class VersionService {

  def version: Json = json"""${BuildInfo.toJson}"""

}
