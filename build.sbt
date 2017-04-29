name := """sbt-gitlab"""
organization := "net.scalytica"
version := "0.1-SNAPSHOT"

scalaVersion := "2.10.6"

sbtPlugin := true

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.1" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

bintrayPackageLabels := Seq("sbt","plugin")
bintrayVcsUrl := Some("""git@gitlab.com:kpmeen/sbt-gitlab.git""")

initialCommands in console := """import net.scalytica.sbt._"""

// set up 'scripted; sbt plugin for testing sbt plugins
ScriptedPlugin.scriptedSettings
scriptedLaunchOpts ++=
  Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + version.value)
