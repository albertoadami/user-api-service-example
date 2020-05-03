package it.adami.api.user.repository

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.Date

import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import it.adami.api.user.DatabaseSpec
import it.adami.api.user.database.DatabaseManager
import it.adami.api.user.domain.User
import org.scalatest.{FutureOutcome, OptionValues}

import scala.util.Random

class DoobieUserRepositorySpec extends DatabaseSpec with ForAllTestContainer with OptionValues {

  override def withFixture(test: NoArgAsyncTest): FutureOutcome = super.withFixture(test)

  private def generateUser = User(
    Random.nextString(5),
    Random.nextString(5),
    Random.nextString(5),
    Random.nextString(5),
    new Date(1990, 6, 8),
    s"${Random.nextString(5)}@test.com",
    Timestamp.valueOf(LocalDateTime.now()),
    false
  )

  override lazy val container: PostgreSQLContainer = postgres

  override def afterStart(): Unit = {
    DatabaseManager.migrateSchema(transactor).unsafeRunSync()//create the table schema
  }

  "DoobieUserRepository" should {

    "insert a user into the users table if the email is not in use" in {
      val userRepository = createRepository

      userRepository
        .insertUser(generateUser)
          .map(value => value.isDefined shouldBe true)
          .unsafeRunSync()

    }

    "not insert a user into the user table if the email is already in use" in {
      val userRepository = createRepository
      val user = generateUser

      userRepository.insertUser(user).unsafeRunSync() // insert the first user
      userRepository.insertUser(generateUser.copy(email = user.email)).map(_.isEmpty shouldBe true).unsafeToFuture()
    }



  }

  private def createRepository: UserRepository = UserRepository(transactor)

}