package it.adami.api.user.repository

import cats.effect.IO
import it.adami.api.user.domain.User
import doobie._
import doobie.implicits._

trait UserRepository {
  def createSchema: IO[Int]
  def insertUser(user: User): IO[Option[Int]]
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
      lastname VARCHAR NOT NULL,
      email  VARCHAR NOT NULL UNIQUE,
      password VARCHAR NOT NULL,
      birthday_date  VARCHAR NOT NULL,
      gender VARCHAR NOT NULL,
      creation_date VARCHAR NOT NULL,
      enabled boolean NOT NULL
    )
  """.update.run
    createTable.transact(xa)
  }

  override def insertUser(user: User): IO[Option[Int]] = {
    val insertQuery =
      sql"""
           INSERT INTO users(firstname, lastname, email, password, birthday_date, gender, creation_date, enabled)
           VALUES(${user.firstname}, ${user.surname}, ${user.email}, ${user.password}, ${user.dateOfBirth}, ${user.gender}, ${user.creationDate}, ${user.enabled})
           """.update
        .withUniqueGeneratedKeys[Int]("id")
        .transact(xa)

    val checkIfExist =
      sql"""SELECT email
            FROM users u
            WHERE u.email = ${user.email}
         """.stripMargin
        .query[User]
        .option
        .transact(xa)

    for {
      exist <- checkIfExist
      result <- exist match {
        case Some(_) => IO { None }
        case None    => insertQuery.map(Some(_))
      }
    } yield result

  }

}
