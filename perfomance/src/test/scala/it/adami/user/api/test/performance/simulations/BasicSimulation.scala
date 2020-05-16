package it.adami.user.api.test.performance.simulations

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.Predef._
import it.adami.user.api.test.performance.endpoints.{UserApiCalls, UserApiServiceConfig}
import io.gatling.core.controller.inject.InjectionProfile
import io.gatling.core.controller.inject.open.RampOpenInjection
import scala.concurrent.duration._

trait BasicSimulation extends Simulation {

  private val config = ConfigFactory.load()
  private val userApiServiceConfig = new UserApiServiceConfig(config)
  protected val userApiCalls = new UserApiCalls(userApiServiceConfig)

  private val users = config.getConfig("simulation").getInt("users")

  protected val configureRampUp: RampOpenInjection =
    rampUsers(users) during 1.minute

  private lazy val versionScenario =
    scenario("VersionInfoScenario").exec {
      userApiCalls.versionInfo
    }

  protected val protocolConf: HttpProtocolBuilder =
    http
      .baseUrl(userApiServiceConfig.url)
      .acceptHeader("application/json")

  protected def mainScenario: PopulationBuilder

  def defaultSetUp(): SetUp = {
    setUp(versionScenario.inject(atOnceUsers(1)), mainScenario)
      .protocols(protocolConf)
  }

}