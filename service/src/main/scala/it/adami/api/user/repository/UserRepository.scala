package it.adami.api.user.repository

import cats.effect.IO
import it.adami.api.user.domain.User
import doobie._
import doobie.implicits._

trait UserRepository {
  def createSchema: IO[Int]
  def insertUser(user: User): IO[Int]
}

object UserRepository {
  def apply(xa: Transactor[IO]): UserRepository = new DoobieUserRepository(xa)
}

final class DoobieUserRepository(xa: Transactor[IO]) extends UserRepository {

  override def createSchema: IO[Int] = {
    val createTable =
      sql"""
    CREATE TABLE users (
      id   SERIAL PRIMARY KEY,
      firstname VARCHAR NOT NULL,
      surname VARCHAR NOT NULL,
      email  VARCHAR NOT NULL UNIQUE
    )
  """.update.run
    createTable.transact(xa)
  }

  override def insertUser(user: User): IO[Int] = {
    sql"INSERT INTO users(firstname, surname, email) VALUES(${user.name}, ${user.surname}, ${user.email})".update
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
  }

}
