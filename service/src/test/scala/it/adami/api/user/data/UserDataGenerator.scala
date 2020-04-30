package it.adami.api.user.data

import it.adami.api.user.http.json.CreateUserRequest

object UserDataGenerator {

  def generateCreateUserRequest: CreateUserRequest =
    CreateUserRequest(
      name = "ciao",
      surname = "pluto",
      email = "email",
      gender = "MALE"
    )

}
