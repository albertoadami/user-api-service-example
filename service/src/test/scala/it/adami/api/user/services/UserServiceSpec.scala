package it.adami.api.user.services

import cats.effect.IO
import it.adami.api.user.SpecBase
import it.adami.api.user.data.UserDataGenerator
import it.adami.api.user.domain.User
import it.adami.api.user.repository.UserRepository
import org.scalatest.EitherValues

import scala.util.Random

class UserServiceSpec extends SpecBase with EitherValues {

  private val userRepository = new UserRepository {
    override def insertUser(user: User): IO[Option[Int]] = IO.pure(Some(1))

    override def createSchema: IO[Int] = IO.pure(Random.nextInt(1))

  }

  "UserService" when {
    "createUser() is called" should {
      "return the generated userId for a new user request" in {
        val userService = new UserService(userRepository)
        userService
          .createUser(UserDataGenerator.generateCreateUserRequest)
          .map { value => value.right.value shouldBe "1" }
          .unsafeToFuture

      }

    }
  }

}
