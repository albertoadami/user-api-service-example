package it.adami.api.user.services

import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import it.adami.api.user.errors.{CreateUserError, UserNameAlreadyInUse}
import it.adami.api.user.http.json.{CreateUserRequest, UserDetailResponse}
import it.adami.api.user.repository.UserRepository
import it.adami.api.user.converters.UserConverters._

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

}
