package it.adami.user.api.test.performance.data

import com.typesafe.scalalogging.LazyLogging
import io.gatling.core.structure.ChainBuilder
import io.gatling.core.Predef._
import it.adami.user.api.test.performance.endpoints.UserApiCalls

import scala.util.Random

class User {
  def email: String = s"test${Random.nextInt()}@test${Random.nextInt()}.it"
  def password: String = s"password${Random.nextInt()}"
}

object TestFeeder extends LazyLogging {

  def generateUser(user: SimulationUser): Iterator[Map[String, String]] = {
    logger.info("Using on-the-fly user generation.")
    Iterator.continually {
      generateValuesForUser(user)
    }
  }

  private def generateValuesForUser(user: SimulationUser): Map[String, String] = {
    val userObject = new User()
    Map(
      user.firstNameKey -> Random.nextString(5),
      user.lastNameKey -> Random.nextString(5),
      user.emailKey -> userObject.email,
      user.passwordKey -> userObject.password
    )
  }

}
