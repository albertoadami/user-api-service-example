package it.adami.api.user.errors

sealed trait CreateUserError

object UserNameAlreadyInUse extends CreateUserError
