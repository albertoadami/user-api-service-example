package it.adami.user.api.test.end.common.containers

import buildinfo.BuildInfo
import com.dimafeng.testcontainers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

trait UserApiContainer extends PostgresContainer {

  private val ExposedPort = 8080

  lazy val userApiContainer: GenericContainer = GenericContainer(
    dockerImage = s"user-api:${BuildInfo.version}",
    exposedPorts = Seq(ExposedPort),
    waitStrategy = Wait.forHttp("/health").forStatusCode(204),
    env = Map(
      "POSTGRES_USER" -> postgresContainer.username,
      "POSTGRES_PASSWORD" -> postgresContainer.password,
      "POSTGRES_URL" -> getJdbcWithIp
    )
  )

  protected val apiVersion = "0.1"

  lazy val serviceHost =
    s"${userApiContainer.containerIpAddress}:${userApiContainer.mappedPort(ExposedPort)}"
  lazy val basePath = s"http://$serviceHost/api/$apiVersion"

  lazy val versionApiPath: String = s"$basePath/version"
  lazy val createUserApiPath: String = s"$basePath/signUp"
  lazy val activateUserApiPath: String = s"$basePath/profile/activate"
  lazy val changePasswordApiPath: String = s"$basePath/profile/password"
  def getUserApiPath(userId: Int): String = s"$basePath/users/$userId"
  def getDeleteApiPath(userId: Int): String = s"$basePath/users/$userId"
  def getUpdateApiPath(userId: Int): String = s"$basePath/users/$userId"

}
