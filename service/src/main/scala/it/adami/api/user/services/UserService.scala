package it.adami.api.user.services

import java.sql.Timestamp
import java.time.LocalDateTime
import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import it.adami.api.user.errors.{CreateUserError, GenericError, UserNameAlreadyInUse, UserNotFound}
import it.adami.api.user.http.json.{CreateUserRequest, UpdateUserRequest, UserDetailResponse}
import it.adami.api.user.repository.UserRepository
import it.adami.api.user.converters.UserConverters._
import it.adami.api.user.util.StringUtils

class UserService(userRepository: UserRepository) extends LazyLogging {

  def createUser(createUserRequest: CreateUserRequest): IO[Either[CreateUserError, Int]] = {

    userRepository.insertUser(createUserRequest).map {
      case Some(id) =>
        logger.info(s"Generated a new user with id $id")
        Right(id)
      case None =>
        Left(UserNameAlreadyInUse)
    }

  }

  def findUser(id: Int): IO[Option[UserDetailResponse]] =
    userRepository.findUser(id).map { result => result.map(convertToUserDetail) }

  def deleteUser(id: Int): IO[Either[GenericError, Unit]] =
    userRepository.deleteUser(id) map { value =>
      if (value == 0) {
        logger.info(s"Not found user with id $id")
        Left(UserNotFound)
      } else Right(())
    }

  def updateUser(id: Int, updateUserRequest: UpdateUserRequest): IO[Either[GenericError, Unit]] =
    userRepository.findUser(id).flatMap {
      case Some(user) =>
        val updatedUser = user
          .copy(
            firstName = updateUserRequest.firstname,
            lastName = updateUserRequest.lastname,
            gender = updateUserRequest.gender,
            dateOfBirth = StringUtils.getDateFromString(updateUserRequest.dateOfBirth),
            lastUpdatedDate = Some(Timestamp.valueOf(LocalDateTime.now()))
          )

        userRepository
          .updateUser(id, updatedUser)
          .map(_ => Right())
      case None =>
        logger.info(s"Not found user with id $id")
        IO(Left(UserNotFound))
    }

}
