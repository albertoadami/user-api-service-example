import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys._
import _root_.sbt.Keys._
import com.typesafe.sbt.packager.linux.LinuxKeys

object CoverageSettings extends  LinuxKeys {

  lazy val settings: Seq[Def.Setting[_]] = Seq(
    coverageEnabled in (Test, test) := true,
    coverageMinimum := 80,
    coverageFailOnMinimum := true,
    coverageExcludedPackages := ".*user;.*user.config"
  )
}
