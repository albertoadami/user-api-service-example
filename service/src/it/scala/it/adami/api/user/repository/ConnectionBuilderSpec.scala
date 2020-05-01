package it.adami.api.user.repository

import cats.effect.{ContextShift, IO}
import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import it.adami.api.user.config.PostgresConfig
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpecLike
import doobie.implicits._

import scala.util.Random

class ConnectionBuilderSpec extends AsyncWordSpecLike with Matchers with ForAllTestContainer {

  implicit val contextShift: ContextShift[IO] = IO.contextShift(executionContext)

  override lazy val container: PostgreSQLContainer = PostgreSQLContainer(databaseName = s"user-${Random.nextInt(5)}")

  "ConnectionHelper" should {
    "create a Hikari transactor from configuration" in {

      val postgresConfig: PostgresConfig = PostgresConfig(
        user = container.username,
        password = container.password,
        jdbcUrl = container.jdbcUrl
      )

      val transactor = ConnectionBuilder.generateTransactor(postgresConfig)
      val simpleQuery = sql"select 42".query[Int].unique

      transactor.use { xa =>

        simpleQuery.transact(xa)

      }.map(_ shouldBe 42)
        .unsafeToFuture()


    }
  }

}
