package it.adami.api.user.repository

import cats.effect.IO
import it.adami.api.user.domain.User
import doobie._

trait UserRepository {
  def insert(user: User): IO[Option[Int]]
  def find(id: Int): IO[Option[User]]
  def findByEmail(email: String): IO[Option[User]]
  def update(id: Int, user: User): IO[Unit]
  def delete(id: Int): IO[Int]
  def search(user: Int, search: String): IO[Seq[User]]
}

object UserRepository {
  def apply(xa: Transactor[IO]): UserRepository = new DoobieUserRepository(xa)
}
