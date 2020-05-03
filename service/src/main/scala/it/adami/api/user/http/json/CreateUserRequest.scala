package it.adami.api.user.http.json

import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf
import cats.effect.IO
import io.circe.generic.auto._

case class CreateUserRequest(
    firstname: String,
    surname: String,
    email: String,
    password: String,
    dateOfBirth: String,
    gender: String
)

object CreateUserRequest {
  implicit val decoder: EntityDecoder[IO, CreateUserRequest] = jsonOf[IO, CreateUserRequest]
}
