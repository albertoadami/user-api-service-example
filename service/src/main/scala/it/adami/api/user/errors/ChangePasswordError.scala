package it.adami.api.user.errors

sealed trait ChangePasswordError

object WrongOldPasswordError extends ChangePasswordError
