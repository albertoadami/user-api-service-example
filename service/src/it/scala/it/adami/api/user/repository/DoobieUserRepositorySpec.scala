package it.adami.api.user.repository

import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import it.adami.api.user.DatabaseSpec
import it.adami.api.user.domain.User
import org.scalatest.OptionValues

import scala.util.Random

class DoobieUserRepositorySpec extends DatabaseSpec with ForAllTestContainer with OptionValues {

  private val user = User(
    Random.nextString(5),
    Random.nextString(5),
    Random.nextString(5),
    Random.nextString(5),
    Random.nextString(5),
    Random.nextString(5),
    Random.nextString(5),
    false
  )

  override lazy val container: PostgreSQLContainer = postgres

  "DoobieUserRepository" should {
    "create the users table schema" in {

      val userRepository = UserRepository(transactor)

      userRepository.createSchema
          .map(_ shouldBe 0)
          .unsafeToFuture
    }

    "insert a user into the users table" in {
      val userRepository = UserRepository(transactor)

      userRepository
        .insertUser(user)
          .map(value => value.isDefined shouldBe true)
          .unsafeToFuture

    }



  }
}