package it.adami.user.api.test.end

import cats.effect.IO
import com.dimafeng.testcontainers.{ForAllTestContainer, LazyContainer, MultipleContainers}
import io.circe.Json
import it.adami.user.api.test.end.common.ClientBuilder
import it.adami.user.api.test.end.common.containers.UserApiContainer
import org.http4s.{BasicCredentials, Headers, Request, Uri}
import org.http4s.client.Client
import org.http4s.dsl.io.POST
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpecLike
import org.http4s.circe._
import org.http4s.headers.Authorization

trait SpecBase
    extends AsyncWordSpecLike
    with Matchers
    with UserApiContainer
    with ForAllTestContainer {

  override val container: MultipleContainers =
    MultipleContainers(LazyContainer(postgresContainer), LazyContainer(userApiContainer))

  protected val client: Client[IO] = ClientBuilder.createClient

  protected def registerUser(createReq: Json): (String, Headers) = {
    val email = createReq.hcursor.get[String]("email").right.get
    val password = createReq.hcursor.get[String]("password").right.get
    val postReq: Request[IO] =
      Request(method = POST, uri = Uri.unsafeFromString(createUserApiPath)).withEntity(createReq)
    client
      .fetch(postReq) { response =>
        val location = response.headers.toList
          .find(_.name.toString == "Location")
          .get
          .value
          .replace("localhost:8080", serviceHost)
        IO.pure(location, Headers.of(Authorization(BasicCredentials(email, password))))
      }
      .unsafeRunSync()
  }

}
