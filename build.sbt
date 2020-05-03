import Dependencies._

val commonSettings = Seq(
  organization := "it.adami",
  scalaVersion := "2.12.6",
  unusedCompileDependenciesFilter -=
    moduleFilter("org.slf4j", "log4j-over-slf4j") ,
  unusedCompileDependenciesFilter -=
    moduleFilter("ch.qos.logback", "logback-classic"),
  unusedCompileDependenciesFilter -=
    moduleFilter("org.tpolecat", "doobie-postgres")
)

val buildInfoSettings = Seq(
  buildInfoOptions += BuildInfoOption.ToMap
)


lazy val service = (project in file("service"))
  .enablePlugins(DockerPlugin, JavaServerAppPackaging, BuildInfoPlugin)
  .settings(
    name := "user-api",
    scalacOptions += "-Ypartial-unification",
    coverageExcludedPackages := ".*user;.*user.config;.*user.http",
      libraryDependencies ++=
      http4sDependencies ++
      loggingDependencies ++
      circeDependencies ++
      databaseDependencies ++
      catsDependencies ++
      testDependencies
  )
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings
  )
  .settings(commonSettings: _*)
  .settings(CoverageSettings.settings: _*)
  .settings(DockerSettings.settings: _*)
  .settings(buildInfoSettings: _*)

lazy val `end-to-end` = (project in file("end-to-end"))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "end-to-end",
    scalacOptions += "-Ypartial-unification",
    libraryDependencies ++=
      http4sDependencies ++
      circeDependencies ++
      testDependencies ++
      loggingDependencies ++
      endToEndDependencies
  )
  .settings(commonSettings: _*)
  .settings(buildInfoSettings: _*)


lazy val `user-api` = (project in file("."))
  .aggregate(service)
  .settings(commonSettings)
  .settings(
    run := {
      (run in service in Compile).evaluated // Enables "sbt run" on the root project
    }
  )
