package it.adami.user.api.test.end

import cats.effect.IO
import com.dimafeng.testcontainers.{ForAllTestContainer, LazyContainer, MultipleContainers}
import it.adami.user.api.test.end.common.ClientBuilder
import it.adami.user.api.test.end.common.containers.UserApiContainer
import org.http4s.client.Client
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpecLike

trait SpecBase extends AsyncWordSpecLike with Matchers with UserApiContainer with ForAllTestContainer {

  override val container: MultipleContainers =
    MultipleContainers(LazyContainer(postgresContainer), LazyContainer(userApiContainer))

  protected val client: Client[IO] = ClientBuilder.createClient

}
