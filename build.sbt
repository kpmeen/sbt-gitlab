name := """sbt-gitlab"""
organization := "net.scalytica"
version := "0.1-SNAPSHOT"

scalaVersion := "2.10.6"

sbtPlugin := true

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-nullary-override",
  "-Ywarn-numeric-widen",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)

val http4sVersion    = "0.15.9a"
val circeVersion     = "0.6.1"
val scalatestVersion = "3.0.1"

libraryDependencies ++= Seq(
  "org.http4s"    %% "http4s-async-http-client" % http4sVersion,
  "org.http4s"    %% "http4s-dsl"               % http4sVersion,
  "org.http4s"    %% "http4s-circe"             % http4sVersion,
  "io.circe"      %% "circe-literal"            % circeVersion,
  "io.circe"      %% "circe-parser"             % circeVersion,
  "io.circe"      %% "circe-generic"            % circeVersion,
  "org.scalactic" %% "scalactic"                % scalatestVersion % Test,
  "org.scalatest" %% "scalatest"                % scalatestVersion % Test
)

bintrayPackageLabels := Seq("sbt", "plugin", "gitlab")
bintrayVcsUrl := Some("""git@gitlab.com:kpmeen/sbt-gitlab.git""")

initialCommands in console := """import net.scalytica.sbt._"""

// set up 'scripted; sbt plugin for testing sbt plugins
ScriptedPlugin.scriptedSettings
scriptedLaunchOpts ++= Seq(
  "-Xmx1024M",
  "-XX:MaxPermSize=256M",
  "-Dplugin.version=" + version.value
)

scriptedBufferLog := false

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
)
