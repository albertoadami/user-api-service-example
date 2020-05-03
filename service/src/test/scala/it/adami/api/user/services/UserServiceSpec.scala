package it.adami.api.user.services

import cats.effect.IO
import it.adami.api.user.SpecBase
import it.adami.api.user.data.UserDataGenerator
import it.adami.api.user.domain.User
import it.adami.api.user.errors.UserNameAlreadyInUse
import it.adami.api.user.repository.UserRepository
import org.scalatest.EitherValues

class UserServiceSpec extends SpecBase with EitherValues {

  private val userRepository = new UserRepository {
    override def insertUser(user: User): IO[Option[Int]] = IO.pure(Some(1))
  }

  private val errorUserRepository = new UserRepository {
    override def insertUser(user: User): IO[Option[Int]] = IO.pure(None)
  }

  "UserService" when {
    "createUser() is called" should {
      "return the generated userId if the email is not in use" in {
        val userService = new UserService(userRepository)
        userService
          .createUser(UserDataGenerator.generateCreateUserRequest)
          .map { value => value.right.value shouldBe "1" }
          .unsafeToFuture

      }

      "return an error if the email is already in use" in {
        val userService = new UserService(userRepository)
        userService
          .createUser(UserDataGenerator.generateCreateUserRequest)
          .map { value => value.left.value shouldBe UserNameAlreadyInUse }
          .unsafeToFuture
      }

    }
  }

}
