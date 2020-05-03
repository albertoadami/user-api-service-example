package it.adami.api.user.http.json

case class CreateUserRequest(
    firstname: String,
    lastname: String,
    email: String,
    password: String,
    dateOfBirth: String,
    gender: String
)
