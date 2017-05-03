import sbt._

lazy val root = (project in file("."))
  .aggregate(gitlabApi, plugin)
  .enablePlugins(CrossPerProjectPlugin)

lazy val gitlabApi = (project in file("gitlab-api")).settings(
  name := """sbt-gitlab-api""",
  scalaVersion := "2.10.6",
  crossScalaVersions ++= Seq("2.11.11", "2.12.2"),
  libraryDependencies ++= Seq(Dependencies.fansi, Dependencies.playJson),
  libraryDependencies ++= Dependencies.http4s,
  libraryDependencies ++= Dependencies.joda,
  libraryDependencies ++= Dependencies.scalaTest
)

lazy val plugin = (project in file("plugin"))
  .settings(ScriptedPlugin.scriptedSettings)
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

//bintrayPackageLabels := Seq("sbt", "plugin", "gitlab")
//bintrayVcsUrl := Some("""git@gitlab.com:kpmeen/sbt-gitlab.git""")
