package it.adami.api.user

import cats.effect.{ContextShift, IO, Resource}
import com.dimafeng.testcontainers.PostgreSQLContainer
import doobie.hikari.HikariTransactor
import it.adami.api.user.config.PostgresConfig
import it.adami.api.user.repository.DatabaseManager
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpecLike

import scala.util.Random

trait DatabaseSpec extends AsyncWordSpecLike with Matchers {

  implicit val contextShift: ContextShift[IO] = IO.contextShift(executionContext)

  lazy val postgres: PostgreSQLContainer = PostgreSQLContainer(databaseName = s"user-${Random.nextInt(5)}")

  protected lazy val transactor: HikariTransactor[IO] = {
    val postgresConfig: PostgresConfig = PostgresConfig(
      user = postgres.username,
      password = postgres.password,
      jdbcUrl = postgres.jdbcUrl
    )
    DatabaseManager
      .generateTransactor(postgresConfig)
      .allocated
      .unsafeRunSync()._1
  }


}
