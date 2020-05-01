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

  lazy val endToEndDependencies = Seq(
    "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.36.1" % Test,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion % Test,
    "org.scalatest" %% "scalatest" % "3.1.1" % Test
  )

  lazy val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.1.1" % Test,
    "org.mockito" %% "mockito-scala" % "1.14.0" % Test
  )

}
