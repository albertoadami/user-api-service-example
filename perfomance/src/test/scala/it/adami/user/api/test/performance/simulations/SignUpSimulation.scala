package it.adami.user.api.test.performance.simulations

import io.gatling.core.Predef.Simulation
import io.gatling.core.structure.{ChainBuilder, PopulationBuilder}
import io.gatling.core.Predef._
import it.adami.user.api.test.performance.data.{SimulationUserImpl, TestFeeder}

import scala.concurrent.duration._

class SignUpSimulation extends Simulation with BasicSimulation {

  private def init(): ChainBuilder = {
    feed(TestFeeder.generateUser(SimulationUserImpl))
      .exec(userApiCalls.signUp(SimulationUserImpl))
      .exec(pace(randomPace))
      .exec(userApiCalls.activate(SimulationUserImpl))
  }

  override protected def mainScenario: PopulationBuilder =
    scenario(this.getClass.getCanonicalName)
      .exitBlockOnFail(init())
      .exitHereIfFailed
      .during(duration minutes, "i") {
        exec {
          pace(randomPace)
        }.exec {
          userApiCalls.profile(SimulationUserImpl)
        }
      }
      .inject(configureRampUp)

  defaultSetUp()
}
