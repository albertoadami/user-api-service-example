package it.adami.bitrock.user.api.test.end

import java.util.concurrent.Executors

import cats.effect.{Blocker, ContextShift, IO}
import com.dimafeng.testcontainers.{ForAllTestContainer, GenericContainer}
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.testcontainers.containers.wait.strategy.Wait

import scala.concurrent.ExecutionContext

trait SpecBase extends AsyncFlatSpec with Matchers { this: ForAllTestContainer =>

  private val blockingPool = Executors.newFixedThreadPool(5)
  private val blocker = Blocker.liftExecutorService(blockingPool)

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.fromExecutor(blockingPool))

  val client: Client[IO] = JavaNetClientBuilder[IO](blocker).create

  override val container = GenericContainer(
    dockerImage = "user-api:0.0.1-SNAPSHOT",
    exposedPorts = Seq(8080),
    waitStrategy = Wait.forHttp("/health").forStatusCode(204)
  )

  protected def serviceHost = s"${container.containerIpAddress}:${container.mappedPort(8080)}"

}
