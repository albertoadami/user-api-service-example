import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.universal.UniversalPlugin.autoImport._
import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}
import com.typesafe.sbt.packager.linux.LinuxKeys
import _root_.sbt._
import _root_.sbt.Keys._

object DockerSettings extends LinuxKeys {

  lazy val settings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := "openjdk:8-slim",
    daemonUserUid in Docker := None,
    daemonUser in Docker := "daemon",
    dockerExposedPorts := Seq(8080)
  )
}
