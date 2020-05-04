package it.adami.api.user.data

import java.sql.Timestamp
import java.util.Date

import it.adami.api.user.domain.User
import it.adami.api.user.http.json.{CreateUserRequest, UserDetailResponse}
import it.adami.api.user.util.StringUtils

import scala.util.Random

object UserDataGenerator {

  def generateCreateUserRequest: CreateUserRequest =
    CreateUserRequest(
      firstname = Random.nextString(5),
      lastname = Random.nextString(5),
      email = "test@mail.com",
      password = Random.nextString(5),
      dateOfBirth = "07-09-1990",
      gender = "MALE"
    )

  def generateUser: User =
    User(
      firstname = Random.nextString(5),
      lastname = Random.nextString(5),
      email = Random.nextString(5),
      password = Random.nextString(5),
      dateOfBirth = StringUtils.getDateFromString("07-07-2007"),
      gender = "MALE",
      creationDate = new Timestamp(System.currentTimeMillis()),
      enabled = true
    )

  def generateUserDetailResponse: UserDetailResponse =
    UserDetailResponse(
      firstname = Random.nextString(5),
      lastname = Random.nextString(5),
      email = Random.nextString(5),
      dateOfBirth = "07-07-2000",
      gender = "MALE",
      creationDate = "09-09-2012"
    )

}
