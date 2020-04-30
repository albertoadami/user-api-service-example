package it.adami.api.user.http.json

import cats.effect.IO
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf
import io.circe.generic.auto._
import org.http4s.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import io.circe.syntax._
import org.http4s.Method._
import cats.effect.IO
import io.circe.generic.auto._
import fs2.Stream

case class CreateUserRequest(name: String, surname: String, email: String, gender: String)

object CreateUserRequest {
  implicit val decoder = jsonOf[IO, CreateUserRequest]
}
