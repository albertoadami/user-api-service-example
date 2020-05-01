package it.adami.api.user.converters

import it.adami.api.user.domain.User
import it.adami.api.user.http.json.CreateUserRequest

object UserConverters {

  implicit def convertToUser(req: CreateUserRequest): User =
    User(
      name = req.name,
      surname = req.surname,
      email = req.email
    )

}
