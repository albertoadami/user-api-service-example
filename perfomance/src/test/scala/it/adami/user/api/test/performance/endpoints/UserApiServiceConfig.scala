package it.adami.user.api.test.performance.endpoints

import com.typesafe.config.Config

class UserApiServiceConfig(config: Config) {

  private val userApiServiceConfig = config.getConfig("user-api-service")
  private val endpoints = userApiServiceConfig.getConfig("endpoints")

  lazy val url: String = userApiServiceConfig.getString("url")
  lazy val version: String = endpoints.getString("version")
  lazy val signUp: String = endpoints.getString("sign-up")
  lazy val activate: String = endpoints.getString("activate")
  lazy val profile: String = endpoints.getString("profile")

}
