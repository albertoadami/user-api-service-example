package it.adami.user.api.test.performance.endpoints

import com.typesafe.scalalogging.LazyLogging
import io.gatling.core.structure.ChainBuilder
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import it.adami.user.api.test.performance.data.SimulationUser
import it.adami.user.api.test.performance.util.StringUtil

class UserApiCalls(userApiServiceConfig: UserApiServiceConfig) extends LazyLogging {

  lazy val getVersionUrl: String = userApiServiceConfig.version
  lazy val signUpUrl: String = userApiServiceConfig.signUp
  lazy val activateUrl: String = userApiServiceConfig.activate
  lazy val profileUrl: String = userApiServiceConfig.profile

  val versionInfo: ChainBuilder = {
    exec {
      http("Version info")
        .get(getVersionUrl)
        .asJson
        .check(jsonPath("$..*").saveAs("versionInfo"))
    }.exec { session =>
        logger.info(s"Version response: ${session("versionInfo").as[String]}")
        session
      }
      .exec { session => session.remove("versionInfo") }
  }

  def signUp(user: SimulationUser): HttpRequestBuilder = {
    http("Sing up")
      .post(signUpUrl)
      .body(StringBody { session =>
        UserApiJson.signUp(
          firstname = session(user.firstNameKey).as[String],
          lastname = session(user.lastNameKey).as[String],
          email = session(user.emailKey).as[String],
          password = session(user.passwordKey).as[String]
        )
      })
      .check(status.is(201))
  }

  def activate(user: SimulationUser): HttpRequestBuilder = {
    http("Activate profile")
      .post(activateUrl)
      .basicAuth(
        username = "${emailKey}",
        password = "${passwordKey}"
      )
      .check(status is (204))
  }

  def profile(user: SimulationUser): HttpRequestBuilder = {
    http("Get profile info")
      .get(profileUrl)
      .basicAuth(
        username = "${emailKey}",
        password = "${passwordKey}"
      )
      .check(status is (200))

  }

}
