package it.adami.api.user.services

import io.circe._
import buildinfo.BuildInfo

class VersionService {

  def version: Json = {
    val buildInfo = BuildInfo.toMap.toSeq.map(p => p._1 -> Json.fromString(p._2.toString))
    Json.obj(buildInfo: _*)
  }

}
