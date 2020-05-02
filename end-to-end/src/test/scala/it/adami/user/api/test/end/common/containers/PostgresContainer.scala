package it.adami.user.api.test.end.common.containers

import com.dimafeng.testcontainers.PostgreSQLContainer
import org.testcontainers.containers.{PostgreSQLContainer => OTCPostgreSQLContainer}

import scala.util.Random
import scala.collection.JavaConverters._

trait PostgresContainer {

  lazy val postgresContainer: PostgreSQLContainer =
    PostgreSQLContainer(databaseName = s"user-${Random.nextInt(5)}")

  def getJdbcWithIp: String = {
    val ip =
      postgresContainer.container.getContainerInfo.getNetworkSettings.getNetworks.asScala.values.head.getIpAddress
    s"jdbc:postgresql://$ip:${OTCPostgreSQLContainer.POSTGRESQL_PORT}/${postgresContainer.databaseName}"

  }

}
