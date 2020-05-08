package it.adami.api.user.http.authentication

import cats.effect.IO
import it.adami.api.user.SpecBase
import it.adami.api.user.data.UserDataGenerator
import it.adami.api.user.domain.User
import it.adami.api.user.repository.UserRepository
import org.http4s.{AuthedRoutes, BasicCredentials, Header, Headers, Request}
import org.http4s.dsl.impl.Root
import org.http4s.dsl.io._
import org.http4s.headers.Authorization
import org.http4s.implicits._

import scala.util.Random

class AuthenticationSpec extends SpecBase {

  private val user = UserDataGenerator.generateUser

  private val userRepository = new UserRepository {
    override def insertUser(user: User): IO[Option[Int]] = IO.pure(None)
    override def findUser(id: Int): IO[Option[User]] = IO.pure(None)
    override def findUserByEmail(email: String): IO[Option[User]] =
      IO.pure(Some(user.copy(email = email)))
    override def updateUser(id: Int, user: User): IO[Unit] = IO.pure(())
    override def deleteUser(id: Int): IO[Int] = IO.pure(1)
  }

  val authentication = new Authentication(userRepository)

  private val simpleTestRoutes: AuthedRoutes[UserInfo, IO] = AuthedRoutes.of {
    case GET -> Root / "test" as user =>
      NoContent()
  }

  private val authTestRoutes = authentication.middleware(simpleTestRoutes).orNotFound

  "Authentication" should {
    "return Forbidden when the Authorization header is not provided" in {

      val result = authTestRoutes.run(Request(uri = uri"/test")).unsafeRunSync
      result.status shouldBe Forbidden
    }

    "return Forbidden when the credentials are incorrect" in {
      val credentials =
        BasicCredentials(Random.nextString(10), user.password + Random.nextString(5)).token
      val headers = Headers.of(Header.apply(Authorization.name.toString, s"Basic: $credentials"))

      val result = authTestRoutes.run(Request(uri = uri"/test").withHeaders(headers)).unsafeRunSync
      result.status shouldBe Forbidden
    }

  }

}
