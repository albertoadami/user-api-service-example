package it.adami.api.user.repository

import cats.effect.IO
import it.adami.api.user.domain.User
import doobie._

trait UserRepository {
  def insertUser(user: User): IO[Option[Int]]
  def findUser(id: Int): IO[Option[User]]
}

object UserRepository {
  def apply(xa: Transactor[IO]): UserRepository = new DoobieUserRepository(xa)
}
