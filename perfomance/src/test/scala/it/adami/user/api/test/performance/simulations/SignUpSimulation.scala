package it.adami.user.api.test.performance.simulations

import io.gatling.core.Predef.Simulation
import io.gatling.core.structure.{ChainBuilder, PopulationBuilder}
import io.gatling.core.Predef._
import it.adami.user.api.test.performance.data.{SimulationUserImpl, TestFeeder}

class SignUpSimulation extends Simulation with BasicSimulation {

  private def init(): ChainBuilder = {
    feed(TestFeeder.generateUser(SimulationUserImpl))
      .exec(userApiCalls.signUp(SimulationUserImpl))
  }

  override protected def mainScenario: PopulationBuilder =
    scenario(this.getClass.getCanonicalName)
      .exitBlockOnFail(init())
      .exec {
        userApiCalls.activate(SimulationUserImpl)
      }
      .inject(configureRampUp)

  defaultSetUp()
}
