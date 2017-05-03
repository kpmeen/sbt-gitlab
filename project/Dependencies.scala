import sbt._

object Dependencies {

  val http4sVersion      = "0.15.9a"
  val playJsonVersion    = "2.6.0-M7"
  val fansiVersion       = "0.2.3"
  val jodaTimeVersion    = "2.9.6"
  val jodaConvertVersion = "1.8.1"
  val scalatestVersion   = "3.0.1"

  val http4s = Seq(
    "org.http4s" %% "http4s-async-http-client" % http4sVersion,
    "org.http4s" %% "http4s-dsl"               % http4sVersion
  )
  val fansi    = "com.lihaoyi"       %% "fansi"     % fansiVersion
  val playJson = "com.typesafe.play" %% "play-json" % playJsonVersion
  val joda = Seq(
    "joda-time" % "joda-time"    % jodaTimeVersion    % Compile,
    "org.joda"  % "joda-convert" % jodaConvertVersion % Compile
  )
  val scalaTest = Seq(
    "org.scalactic" %% "scalactic" % scalatestVersion % Test,
    "org.scalatest" %% "scalatest" % scalatestVersion % Test
  )

}
