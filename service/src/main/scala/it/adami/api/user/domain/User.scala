package it.adami.api.user.domain

import java.sql.Timestamp

final case class User(
    firstname: String,
    surname: String,
    email: String,
    password: String,
    dateOfBirth: String,
    gender: String,
    creationDate: Timestamp,
    enabled: Boolean
)
