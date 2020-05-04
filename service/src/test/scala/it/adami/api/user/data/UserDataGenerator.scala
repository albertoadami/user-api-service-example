package it.adami.api.user.data

import it.adami.api.user.http.json.CreateUserRequest

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

}
