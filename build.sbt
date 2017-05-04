import sbt._
import Dependencies._
import Publishing._

lazy val BaseSettings = Seq(
  organization := "net.scalytica",
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
  ),
  licenses += "Apache-2.0" -> url(
    "http://www.apache.org/licenses/LICENSE-2.0.html"
  )
)

lazy val root = (project in file("."))
  .settings(BaseSettings: _*)
  .settings(NoopPublishSettings: _*)
  .settings(ReleaseSettings: _*)
  .aggregate(gitlabApi, plugin)
  .enablePlugins(CrossPerProjectPlugin)

lazy val gitlabApi = (project in file("gitlab-api"))
  .settings(PluginSettings: _*)
  .settings(
    name := """sbt-gitlab-api""",
    scalaVersion := "2.10.6",
    crossScalaVersions ++= Seq("2.11.11", "2.12.2"),
    libraryDependencies ++= Seq(fansi, playJson),
    libraryDependencies ++= http4s,
    libraryDependencies ++= joda,
    libraryDependencies ++= scalaTest
  )

lazy val plugin = (project in file("plugin"))
  .settings(ScriptedPlugin.scriptedSettings)
  .settings(LibSettings: _*)
  .settings(
    name := """sbt-gitlab""",
    sbtPlugin := true,
    scalaVersion := "2.10.6",
    scriptedLaunchOpts ++= Seq(
      "-Xmx1024M",
      "-XX:MaxPermSize=256M",
      "-Dplugin.version=" + version.value
    ),
    scriptedBufferLog := false,
    initialCommands in console := """import net.scalytica.sbt._""",
    publishLocal := publishLocal.dependsOn(publishLocal in gitlabApi).value
  )
  .dependsOn(gitlabApi)
