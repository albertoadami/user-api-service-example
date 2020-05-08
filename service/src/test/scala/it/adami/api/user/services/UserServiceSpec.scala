package it.adami.api.user.services

import cats.effect.IO
import it.adami.api.user.SpecBase
import it.adami.api.user.data.UserDataGenerator
import it.adami.api.user.domain.User
import it.adami.api.user.errors.{UserNameAlreadyInUse, UserNotFound}
import it.adami.api.user.repository.UserRepository
import org.scalatest.EitherValues

class UserServiceSpec extends SpecBase with EitherValues {

  private val userRepository = new UserRepository {
    override def insertUser(user: User): IO[Option[Int]] = IO.pure(Some(1))

    override def findUser(id: Int): IO[Option[User]] = IO.pure(Some(UserDataGenerator.generateUser))

    override def deleteUser(id: Int): IO[Int] = IO.pure(1)
  }

  private val errorUserRepository = new UserRepository {
    override def insertUser(user: User): IO[Option[Int]] = IO.pure(None)

    override def findUser(id: Int): IO[Option[User]] = IO.pure(None)

    override def deleteUser(id: Int): IO[Int] = IO.pure(0)
  }

  val userService = new UserService(userRepository)
  val errorUserService = new UserService(errorUserRepository)

  "UserService" when {
    "createUser() is called" should {
      "return the generated userId if the email is not in use" in {
        userService
          .createUser(UserDataGenerator.generateCreateUserRequest)
          .map { value => value.right.value shouldBe 1 }
          .unsafeToFuture

      }

      "return an error if the email is already in use" in {
        userService
          .createUser(UserDataGenerator.generateCreateUserRequest)
          .map { value => value.left.value shouldBe UserNameAlreadyInUse }
          .unsafeToFuture
      }

    }

    "findUser() is called" should {
      "return the user information for an existing id" in {
        userService
          .findUser(999)
          .map(_.isDefined shouldBe true)
          .unsafeToFuture
      }

      "return None if the id is invalid" in {
        errorUserService
          .findUser(9999)
          .map(_.isEmpty shouldBe true)
          .unsafeToFuture
      }
    }

    "deleteUser() is called" should {
      "return unit if the id exists" in {
        userService
          .deleteUser(999)
          .map(_.isRight shouldBe true)
          .unsafeToFuture
      }
      "return UserNotFound if the id doesn't exist" in {
        errorUserService
          .deleteUser(999)
          .map(value => value.left.value shouldBe UserNotFound)
          .unsafeToFuture
      }
    }

  }

}
