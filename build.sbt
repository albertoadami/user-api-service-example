val commonSettings = Seq(
  organization := "it.adami",
  scalaVersion := "2.12.6",
  unusedCompileDependenciesFilter -=
    moduleFilter("org.slf4j", "log4j-over-slf4j") ,
  unusedCompileDependenciesFilter -=
    moduleFilter("ch.qos.logback", "logback-classic")
)

val coverageSettings = Seq(
  coverageEnabled := true,
  coverageMinimum := 80,
  coverageFailOnMinimum := true,
  coverageExcludedPackages := ".*user;.*user.config"
)


val dockerSettings = Seq(
  dockerBaseImage := "openjdk:8-slim",
  daemonUserUid in Docker := None,
  daemonUser in Docker    := "daemon",
  dockerExposedPorts := Seq(8080)
)


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
  "io.circe"   %% "circe-generic" % "0.13.0",
  "io.circe" %% "circe-literal" % "0.13.0"
)

lazy val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.mockito" %% "mockito-scala" % "1.14.0" % Test
)

lazy val service = (project in file("service"))
  .enablePlugins(DockerPlugin, JavaServerAppPackaging, BuildInfoPlugin)
  .settings(commonSettings)
  .settings(dockerSettings)
  .settings(
    name := "user-api",
    scalacOptions += "-Ypartial-unification",
    buildInfoOptions += BuildInfoOption.ToJson,
    libraryDependencies ++=
      http4sDependencies ++
      loggingDependencies ++
      circeDependencies ++
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
