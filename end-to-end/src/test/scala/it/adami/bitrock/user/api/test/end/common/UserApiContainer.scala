package it.adami.bitrock.user.api.test.end.common

import buildinfo.BuildInfo
import com.dimafeng.testcontainers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

trait UserApiContainer {

  private val ExposedPort = 8080

  val container: GenericContainer = GenericContainer(
    dockerImage = s"user-api:${BuildInfo.version}",
    exposedPorts = Seq(ExposedPort),
    waitStrategy = Wait.forHttp("/health").forStatusCode(204)
  )

  lazy val serviceHost = s"${container.containerIpAddress}:${container.mappedPort(ExposedPort)}"
  lazy val basePath = s"http://$serviceHost/api/0.1"

  lazy val versionApiPath: String = s"$basePath/version"

}
