import sbt._

object Dependencies {

  val http4sVersion = "0.21.33"
  lazy val http4sDependencies = Seq(
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion
  )

  lazy val loggingDependencies = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "org.slf4j" % "log4j-over-slf4j" % "2.0.1",
    "ch.qos.logback" % "logback-classic" % "1.2.11"
  )

  lazy val circeDependencies = Seq(
    "io.circe" %% "circe-config" % "0.8.0",
    "io.circe" %% "circe-generic" % "0.14.3"
  )

  val doobieVersion = "0.13.4"
  val databaseDependencies = Seq(
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-hikari" % doobieVersion,
    "org.tpolecat" %% "doobie-postgres" % doobieVersion,
    "org.flywaydb" % "flyway-core" % "6.3.1"
  )

  lazy val catsDependencies = Seq(
    "org.typelevel" %% "cats-core" % "2.1.1"
  )

  lazy val endToEndDependencies = Seq(
    "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.40.10" % Test,
    "com.dimafeng" %% "testcontainers-scala-postgresql" % "0.40.10" % Test,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion % Test,
    "org.scalatest" %% "scalatest" % "3.2.13" % Test
  )

  val scalatestVersion = "3.2.13"
  val testContainerScalaVersion = "0.36.1"
  lazy val testDependencies = Seq(
    "com.dimafeng" %% "testcontainers-scala-scalatest" % testContainerScalaVersion % IntegrationTest,
    "com.dimafeng" %% "testcontainers-scala-postgresql" % testContainerScalaVersion % IntegrationTest,
    "org.scalatest" %% "scalatest" % scalatestVersion % Test,
    "org.scalatest" %% "scalatest" % scalatestVersion % IntegrationTest,
    "org.mockito" %% "mockito-scala" % "1.17.12" % Test,
    "org.tpolecat" %% "doobie-scalatest" % doobieVersion % IntegrationTest
  )

  val gatlingVersion = "3.3.1"
  lazy val performanceDependencies = Seq(
    "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % Test,
    "io.gatling" % "gatling-test-framework" % gatlingVersion % Test
  )

}
