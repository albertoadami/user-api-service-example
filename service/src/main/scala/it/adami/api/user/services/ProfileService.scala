package it.adami.api.user.services

import java.sql.Timestamp
import java.time.LocalDateTime

import cats.effect.IO
import it.adami.api.user.errors.{ChangePasswordError, WrongOldPasswordError}
import it.adami.api.user.repository.UserRepository
import it.adami.api.user.converters.UserConverters._
import it.adami.api.user.http.json.UserProfileResponse

class ProfileService(userRepository: UserRepository) {

  def getProfile(id: Int): IO[UserProfileResponse] =
    userRepository.findUser(id).map { result =>
      val user = result.get
      convertToUserProfile(user)
    }

  def activateUser(id: Int): IO[Unit] =
    userRepository.findUser(id) flatMap {
      case Some(user) =>
        val updatedUser = user.copy(
          enabled = true,
          lastUpdatedDate = Some(Timestamp.valueOf(LocalDateTime.now()))
        )
        userRepository
          .updateUser(id, updatedUser)
          .map(_ => Right())
    }

  def changePassword(userId: Int, oldPassword: String, newPassword: String): IO[Either[ChangePasswordError, Unit]] = {

    userRepository.findUser(userId) flatMap {
      case Some(user) =>
        if (user.password != oldPassword) IO(Left(WrongOldPasswordError))
        else {
          val newUser = user.copy(password = newPassword)
          userRepository
            .updateUser(userId, newUser)
            .map(_ => Right())
        }
    }
  }

}