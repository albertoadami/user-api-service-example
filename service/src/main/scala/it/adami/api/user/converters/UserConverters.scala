package it.adami.api.user.converters

import java.sql.Timestamp
import java.time.LocalDateTime

import it.adami.api.user.domain.User
import it.adami.api.user.http.json.CreateUserRequest
import it.adami.api.user.util.StringUtils

object UserConverters {

  implicit def convertToUser(req: CreateUserRequest): User =
    User(
      firstname = req.firstname,
      surname = req.lastname,
      email = req.email,
      password = req.password,
      dateOfBirth = StringUtils.getDateFromString(req.dateOfBirth),
      creationDate = Timestamp.valueOf(LocalDateTime.now()),
      gender = req.gender,
      enabled = false
    )

}
