package it.adami.api.user.http.json

case class UserDetailResponse(
    firstname: String,
    lastname: String,
    email: String,
    dateOfBirth: String,
    gender: String,
    creationDate: String
)
