package it.adami.api.user.services

import cats.effect.IO
import it.adami.api.user.SpecBase
import it.adami.api.user.data.UserDataGenerator
import it.adami.api.user.domain.User
import it.adami.api.user.errors.WrongOldPasswordError
import it.adami.api.user.repository.UserRepository
import org.scalatest.OptionValues

import scala.util.Random

class ProfileServiceSpec extends SpecBase with OptionValues {

  private val testUser = UserDataGenerator.generateUser.copy(id = Some(Random.nextInt()))

  private val userRepository = new UserRepository {
    override def insertUser(user: User): IO[Option[Int]] = IO.pure(None)

    override def findUser(id: Int): IO[Option[User]] = IO.pure(Some(testUser))

    override def findUserByEmail(email: String): IO[Option[User]] = IO.pure(None)

    override def updateUser(id: Int, user: User): IO[Unit] = IO.pure(())

    override def deleteUser(id: Int): IO[Int] = IO(Random.nextInt(1))

    override def searchUsers(user: Int, search: String): IO[Seq[User]] = IO.pure(Seq.empty)
  }
  private val profileService = new ProfileService(userRepository)

  "ProfileService" when {
    "getProfile() is called" should {
      "return the info of the logged user" in {
        val result = profileService
          .getProfile(999)
          .unsafeRunSync

        result.id shouldBe testUser.id.get
        result.firstname shouldBe testUser.firstName
        result.lastname shouldBe testUser.lastName
        result.email shouldBe testUser.email
        result.gender shouldBe testUser.gender
        result.enabled shouldBe testUser.enabled

      }
    }

    "activateUser() is called" should {
      "return Unit" in {
        profileService
          .activateUser(999)
          .unsafeRunSync
        1 shouldBe 1 //FIXME understand how to improve this test
      }
    }

    "changePassword() is called" should {
      "return WrongOldPasswordError if the old password is not correct" in {
        val result = profileService
          .changePassword(testUser.id.value, Random.nextString(10), Random.nextString(8))
          .unsafeRunSync
        result shouldBe Left(WrongOldPasswordError)
      }
      "return Unit if the old password is correct" in {
        val result = profileService
          .changePassword(testUser.id.value, testUser.password, Random.nextString(8))
          .unsafeRunSync
        result.isRight shouldBe true
      }
    }
  }

}
