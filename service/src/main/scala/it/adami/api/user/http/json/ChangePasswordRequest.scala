package it.adami.api.user.http.json

case class ChangePasswordRequest(oldPassword: String, newPassword: String)
