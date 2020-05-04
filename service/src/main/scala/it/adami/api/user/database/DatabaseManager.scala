package it.adami.api.user.database

import cats.effect.{Blocker, ContextShift, IO, Resource}
import doobie.hikari.HikariTransactor
import it.adami.api.user.config.PostgresConfig
import org.flywaydb.core.Flyway

import scala.concurrent.ExecutionContext

object DatabaseManager {

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

  def migrateSchema(transactor: HikariTransactor[IO]): IO[Unit] = {
    transactor.configure { dataSource =>
      IO {
        val flyWay = Flyway.configure().dataSource(dataSource).load()
        flyWay.migrate()
        ()
      }
    }
  }
}
