package it.adami.api.user.repository

import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import doobie.implicits._
import it.adami.api.user.DatabaseSpec

class DatabaseManagerSpec extends DatabaseSpec with ForAllTestContainer {

  override lazy val container: PostgreSQLContainer = postgres

  "DatabaseManager" should {
    "create a Hikari transactor from configuration" in {

      val simpleQuery = sql"select 42".query[Int].unique//some simple query to check if database is up

      simpleQuery.transact(transactor)
        .map(_ shouldBe 42)
        .unsafeToFuture()

    }

  }

}
