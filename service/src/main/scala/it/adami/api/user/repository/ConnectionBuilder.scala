package it.adami.api.user.repository

import cats.effect.{Blocker, ContextShift, IO, Resource}
import doobie.hikari.HikariTransactor
import it.adami.api.user.config.PostgresConfig

import scala.concurrent.ExecutionContext

object ConnectionBuilder {

  private val PostgresDriver = "org.postgresql.Driver"

  def generateTransactor(postgresConfig: PostgresConfig)(
      implicit contextShift: ContextShift[IO],
      executionContext: ExecutionContext
  ): Resource[IO, HikariTransactor[IO]] = {
    for {
      be <- Blocker[IO]
      xa <- HikariTransactor.newHikariTransactor[IO](
        PostgresDriver,
        postgresConfig.jdbcUrl,
        postgresConfig.user,
        postgresConfig.password,
        executionContext,
        be
      )
    } yield xa

  }
}
