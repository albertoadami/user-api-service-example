package it.adami.api.user.errors

sealed trait GenericError

case object UserNotFound extends GenericError
