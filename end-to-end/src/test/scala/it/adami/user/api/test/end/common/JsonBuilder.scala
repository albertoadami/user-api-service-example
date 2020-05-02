package it.adami.user.api.test.end.common

import io.circe.Json

import scala.util.Random

object JsonBuilder {

  def createRequestJson: Json =
    Json.obj(
      "name" -> Json.fromString(Random.nextString(5)),
      "surname" -> Json.fromString(Random.nextString(5)),
      "email" -> Json.fromString(Random.nextString(5)),
      "gender" -> Json.fromString("M")
    )

}
