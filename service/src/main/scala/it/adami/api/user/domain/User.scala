package it.adami.api.user.domain

final case class User(
    firstname: String,
    surname: String,
    email: String,
    password: String,
    dateOfBirth: String,
    gender: String,
    creationDate: String,
    enabled: Boolean
)
