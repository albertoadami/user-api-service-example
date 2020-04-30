package it.adami.api.user.repository

import cats.effect.{Blocker, ContextShift, IO, Resource}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.Transactor
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor.Aux
import io.circe.config
import it.adami.api.user.config.PostgresConfig

import scala.concurrent.ExecutionContext

object ConnectionHelper {

  private val PostgresDriver = "org.postgresql.Driver"

  def generateTransactor(postgresConfig: PostgresConfig)(
      implicit contextShift: ContextShift[IO],
      executionContext: ExecutionContext
  ): Resource[IO, HikariTransactor[IO]] = {
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](32)
      be <- Blocker[IO]
      xa <- HikariTransactor.newHikariTransactor[IO](
        PostgresDriver,
        s"jdbc:postgresql://${postgresConfig.host}:${postgresConfig.port}/${postgresConfig.database}",
        postgresConfig.user,
        postgresConfig.password,
        ce,
        be
      )
    } yield xa

  }
}
