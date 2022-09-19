import sbt._
import scoverage.ScoverageKeys._
import com.typesafe.sbt.packager.linux.LinuxKeys

object CoverageSettings extends LinuxKeys {

  lazy val settings: Seq[Def.Setting[_]] = Seq(
    coverageMinimum := 80,
    coverageFailOnMinimum := true
  )
}
