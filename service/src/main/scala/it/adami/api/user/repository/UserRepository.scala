package it.adami.api.user.repository

import cats.effect.IO
import it.adami.api.user.domain.User
import doobie._
import doobie.implicits._

trait UserRepository {
  def insertUser(user: User): IO[Int]
}

object UserRepository {
  def apply(xa: Transactor[IO]): UserRepository = new DoobieUserRepository(xa)
}

final class DoobieUserRepository(xa: Transactor[IO]) extends UserRepository {

  override def insertUser(user: User): IO[Int] =
    sql"INSERT INTO users(name, surname, email) VALUES(${user.name}, ${user.surname}, ${user.email})".update
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)

}
