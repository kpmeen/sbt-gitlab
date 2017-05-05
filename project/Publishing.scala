import sbt._
import sbt.Keys._
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._
import bintray.BintrayKeys._

object Publishing {

  /* `publish` performs a no-op */
  val NoopPublishSettings = Seq(
    packagedArtifacts in RootProject(file(".")) := Map.empty,
    publish := (),
    publishLocal := (),
    publishArtifact := false,
    publishTo := None
  )

  val LibSettings = Seq(
    bintrayRepository := "maven",
    bintrayPackage := "sbt-gitlab-api"
  )

  val PluginSettings = Seq(
    bintrayRepository := "sbt-plugins",
    bintrayPackage := "sbt-gitlab"
  )

  val PublishSettings = Seq(
    pomExtra :=
      <url>https://gitlab.com/kpmeen/sbt-gitlab</url>
        <scm>
          <url>git@gitlab.com:kpmeen/sbt-gitlab.git</url>
          <connection>scm:git:git@gitlab.com:kpmeen/sbt-gitlab.git</connection>
        </scm>
        <developers>
          <developer>
            <id>kpmeen</id>
            <name>Knut Petter Meen</name>
            <url>http://scalytica.net</url>
          </developer>
        </developers>,
    autoAPIMappings := true,
    pomIncludeRepository := { _ =>
      false
    },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishArtifact in (Compile, packageDoc) := true,
    publishArtifact in (Compile, packageSrc) := true
  )

  val ReleaseSettings = Seq(
    releaseCrossBuild := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      releaseStepCommandAndRemaining("+test"),
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("+publish"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )
}
