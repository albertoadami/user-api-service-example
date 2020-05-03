import sbt._

object Dependencies {

  val http4sVersion = "0.21.4"
  lazy val http4sDependencies = Seq(
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion
  )

  lazy val loggingDependencies = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "org.slf4j" % "log4j-over-slf4j" % "1.7.30",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )

  lazy val circeDependencies = Seq(
    "io.circe" %% "circe-config" % "0.7.0",
    "io.circe"   %% "circe-generic" % "0.13.0"
  )

  val doobieVersion = "0.8.8"
  val databaseDependencies = Seq(
    "org.tpolecat" %% "doobie-core"      % doobieVersion,
    "org.tpolecat" %% "doobie-postgres"  % doobieVersion,
    "org.tpolecat"          %% "doobie-hikari"          % doobieVersion,
    "org.flywaydb"          %  "flyway-core"          % "6.3.1"
  )

  lazy val catsDependencies = Seq(
    "org.typelevel" %% "cats-core" % "2.1.1"
  )

  lazy val endToEndDependencies = Seq(
    "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.36.1" % Test,
    "com.dimafeng" %% "testcontainers-scala-postgresql" % "0.36.1" % Test,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion % Test,
    "org.scalatest" %% "scalatest" % "3.1.1" % Test
  )

  lazy val testDependencies = Seq(
    "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.36.1" % IntegrationTest,
    "com.dimafeng" %% "testcontainers-scala-postgresql" % "0.36.1" % IntegrationTest,
    "org.scalatest" %% "scalatest" % "3.1.1" % Test,
    "org.scalatest" %% "scalatest" % "3.1.1" % IntegrationTest,
    "org.mockito" %% "mockito-scala" % "1.14.0" % Test,
    "org.tpolecat" %% "doobie-scalatest" % doobieVersion % IntegrationTest
  )

}
