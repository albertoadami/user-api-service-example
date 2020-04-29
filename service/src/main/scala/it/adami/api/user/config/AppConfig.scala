package it.adami.api.user.config

import cats.effect.IO
import io.circe.config.parser
import io.circe.generic.auto._

case class AppConfig(service: ServiceConfig)

object AppConfig {
  def load: IO[AppConfig] = parser.decodeF[IO, AppConfig]()
}
