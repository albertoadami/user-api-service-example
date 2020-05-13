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

    override def updateUser(id: Int, user: User): IO[Unit] = IO.pure(())

    override def findUserByEmail(email: String): IO[Option[User]] =
      IO.pure(Some(UserDataGenerator.generateUser))
  }

  private val errorUserRepository = new UserRepository {
    override def insertUser(user: User): IO[Option[Int]] = IO.pure(None)

    override def findUser(id: Int): IO[Option[User]] = IO.pure(None)

    override def deleteUser(id: Int): IO[Int] = IO.pure(0)

    override def updateUser(id: Int, user: User): IO[Unit] = IO.pure(())

    override def findUserByEmail(email: String): IO[Option[User]] = IO.pure(None)
  }

  private val userService = new UserService(userRepository)
  private val errorUserService = new UserService(errorUserRepository)

  "UserService" when {
    "createUser() is called" should {
      "return the generated userId if the email is not in use" in {
        val result =
          userService
            .createUser(UserDataGenerator.generateCreateUserRequest)
            .unsafeRunSync
        result.right.value shouldBe 1
      }

      "return an error if the email is already in use" in {
        val result =
          errorUserService
            .createUser(UserDataGenerator.generateCreateUserRequest)
            .unsafeRunSync
        result.left.value shouldBe UserNameAlreadyInUse
      }

    }

    "findUser() is called" should {
      "return the user information for an existing id" in {
        val result =
          userService
            .findUser(999)
            .unsafeRunSync
        result.isDefined shouldBe true
      }

      "return None if the id is invalid" in {
        val result =
          errorUserService
            .findUser(9999)
            .unsafeRunSync
        result.isEmpty shouldBe true
      }
    }

    "deleteUser() is called" should {
      "return unit if the id exists" in {
        val result =
          userService
            .deleteUser(999)
            .unsafeRunSync
        result.isRight shouldBe true
      }
      "return UserNotFound if the id doesn't exist" in {
        val result =
          errorUserService
            .deleteUser(999)
            .unsafeRunSync
        result.left.value shouldBe UserNotFound
      }
    }

    "updateUser() is called" should {
      "return Unit if update go right" in {
        val result =
          userService
            .updateUser(999, UserDataGenerator.generateUpdateUserRequest)
            .unsafeRunSync
        result.isRight shouldBe true
      }

      "return UserNotFound if the id doesn't exist" in {
        val result =
          errorUserService
            .updateUser(999, UserDataGenerator.generateUpdateUserRequest)
            .unsafeRunSync
        result.left.value shouldBe UserNotFound
      }
    }

  }

}
