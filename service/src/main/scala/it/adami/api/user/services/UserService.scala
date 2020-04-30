package it.adami.api.user.services

import cats.effect.IO
import it.adami.api.user.errors.CreateUserError
import it.adami.api.user.http.json.CreateUserRequest

class UserService {

  def createUser(createUserRequest: CreateUserRequest): IO[Either[CreateUserError, String]] =
    IO.pure(Right("my-id"))

}
