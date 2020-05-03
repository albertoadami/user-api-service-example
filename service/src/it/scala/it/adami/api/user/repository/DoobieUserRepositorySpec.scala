package it.adami.api.user.repository

import java.sql.Timestamp
import java.time.LocalDateTime

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
    Timestamp.valueOf(LocalDateTime.now()),
    false
  )

  override lazy val container: PostgreSQLContainer = postgres

  "DoobieUserRepository" should {
    "create the users table schema" in {

      val userRepository = UserRepository(transactor)
      DatabaseManager.migrateSchema(transactor).map(_ => 1 shouldBe 1).unsafeToFuture()

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