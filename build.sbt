val commonSettings = Seq(
  organization := "it.adami",
  scalaVersion := "2.12.6"
)

val coverageSettings = Seq(
  coverageEnabled := true,
  coverageMinimum := 80,
  coverageFailOnMinimum := true,
  coverageExcludedPackages := ".*user"
)

val dockerSettings = Seq(
  dockerBaseImage := "openjdk:8-slim",
  daemonUserUid in Docker := None,
  daemonUser in Docker    := "daemon",
  dockerExposedPorts := Seq(8080)
)

val dockerSettings = Seq(
  dockerBaseImage := "openjdk:8-slim",
  daemonUserUid in Docker := None,
  daemonUser in Docker    := "daemon",
  dockerExposedPorts := Seq(8080)
)


val http4sVersion = "0.21.1"
lazy val http4sDependencies = Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion
)

lazy val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.1.1" % "test"
)

lazy val service = (project in file("service"))
  .enablePlugins(DockerPlugin, JavaServerAppPackaging)
  .settings(commonSettings)
  .settings(dockerSettings)
  .settings(
    name := "user-api",
    scalacOptions += "-Ypartial-unification",
    libraryDependencies ++=
      http4sDependencies ++ 
      testDependencies
  )
  .settings(coverageSettings: _*)


lazy val `user-api` = (project in file("."))
  .aggregate(service)
  .settings(commonSettings)
  .settings(
    run := {
      (run in service in Compile).evaluated // Enables "sbt run" on the root project
    }
  )
