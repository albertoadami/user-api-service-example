package it.adami.api.user.services

import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import it.adami.api.user.errors.{CreateUserError, UserNameAlreadyInUse}
import it.adami.api.user.http.json.CreateUserRequest
import it.adami.api.user.repository.UserRepository
import it.adami.api.user.converters.UserConverters._

class UserService(userRepository: UserRepository) extends LazyLogging {

  def createUser(createUserRequest: CreateUserRequest): IO[Either[CreateUserError, String]] = {

    userRepository.insertUser(createUserRequest).map {
      case Some(id) =>
        logger.info(s"Generated a new user with id $id")
        Right(id.toString)
      case None =>
        Left(UserNameAlreadyInUse)
    }

  }

}
