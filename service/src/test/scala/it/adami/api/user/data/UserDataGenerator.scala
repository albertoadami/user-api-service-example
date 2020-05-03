package it.adami.api.user.data

import it.adami.api.user.http.json.CreateUserRequest

import scala.util.Random

object UserDataGenerator {

  def generateCreateUserRequest: CreateUserRequest =
    CreateUserRequest(
      firstname = Random.nextString(5),
      surname = Random.nextString(5),
      email = Random.nextString(5),
      password = Random.nextString(5),
      dateOfBirth = Random.nextString(5),
      gender = "MALE"
    )

}
