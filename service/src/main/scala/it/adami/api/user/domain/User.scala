package it.adami.api.user.domain

import java.sql.Timestamp
import java.util.Date

final case class User(
    firstname: String,
    surname: String,
    email: String,
    password: String,
    dateOfBirth: Date,
    gender: String,
    creationDate: Timestamp,
    enabled: Boolean
)
