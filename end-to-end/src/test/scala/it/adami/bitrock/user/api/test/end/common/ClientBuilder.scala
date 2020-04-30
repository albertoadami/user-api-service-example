package it.adami.bitrock.user.api.test.end.common

import java.util.concurrent.Executors

import cats.effect.{Blocker, ContextShift, IO}
import org.http4s.client.{Client, JavaNetClientBuilder}

import scala.concurrent.ExecutionContext

object ClientBuilder {

  private val blockingPool = Executors.newFixedThreadPool(5)
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.fromExecutor(blockingPool))

  def createClient: Client[IO] = {
    val blocker = Blocker.liftExecutorService(blockingPool)
    JavaNetClientBuilder[IO](blocker).create
  }

}
