import sbt.Keys.scalaVersion

val commonSettings = Seq(
  organization := "it.adami",
  scalaVersion := "2.12.6",
  version := "0.1"
)


val http4sVersion = "0.21.1"
lazy val http4sDependencies = Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion
)

lazy val service = (project in file("service"))
  .settings(commonSettings)
  .settings(
    name := "user-api",
    libraryDependencies ++= http4sDependencies
  )


lazy val root = (project in file("."))
  .aggregate(service)
  .settings(commonSettings)
  .settings(
    run := {
      (run in service in Compile).evaluated // Enables "sbt run" on the root project
    }
  )
