package it.adami.api.user.repository

import cats.effect.IO
import it.adami.api.user.domain.User
import doobie._
import doobie.implicits._
import doobie.implicits.javasql._

trait UserRepository {
  def insertUser(user: User): IO[Option[Int]]
}

object UserRepository {
  def apply(xa: Transactor[IO]): UserRepository = new DoobieUserRepository(xa)
}

final class DoobieUserRepository(xa: Transactor[IO]) extends UserRepository {

  override def insertUser(user: User): IO[Option[Int]] = {
    val insertQuery =
      sql"""
           INSERT INTO users(firstname, lastname, email, password, birthday_date, gender, creation_date, enabled)
           VALUES(${user.firstname}, ${user.surname}, ${user.email}, ${user.password}, ${user.dateOfBirth}, ${user.gender}, ${user.creationDate}, ${user.enabled})
           """.update
        .withUniqueGeneratedKeys[Int]("id")
        .transact(xa)

    val checkIfExist =
      sql"""SELECT firstname, lastname, email, password, birthday_date, gender, creation_date, enabled
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
