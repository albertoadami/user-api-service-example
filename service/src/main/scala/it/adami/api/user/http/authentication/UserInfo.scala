package it.adami.api.user.http.authentication

/**
  * Class used by routes that are using the auth middleware, to get the user information already in "session"
  * @param email the email of the user logged
  * @param enabled status of the user logged
  */
case class UserInfo(email: String, enabled: Boolean)
