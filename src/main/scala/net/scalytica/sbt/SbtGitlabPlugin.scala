package net.scalytica.sbt

import io.circe.Printer
import net.scalytica.sbt.api.APIVersions._
import net.scalytica.sbt.api.{
  GitlabClient,
  Pipelines,
  Projects,
  UsersAndGroups
}
import net.scalytica.sbt.models.{AccessToken, Project, ProjectId}
import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin
import complete.DefaultParsers._
import sbt.complete.Parser.token

import scala.util.Properties

object SbtGitlabPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = JvmPlugin

  trait SettingKeys {

    // Settings keys to configure the plugin
    val gitlabAuthToken = settingKey[String]("Personal GitLab access token.")
    val gitlabUseTLS = settingKey[Boolean](
      "Whether or not to use HTTP or HTTPS when communicating with the GitLab API."
    )
    val gitlabHost = settingKey[String](
      "The hostname for the GitLab instance. Defaults to gitlab.com."
    )
    val gitlabApiVersion = settingKey[APIVersion](
      "The API version to use. Defaults to v4."
    )
    val gitlabBaseUrl = settingKey[String]("The base URL for the gitlab API.")
    val gitlabProjectNamespace = settingKey[String](
      "The GitLab namespace for this project. The namespace is typically the " +
        "name of the repos owner."
    )
    val gitlabProjectOwnerIsUser = settingKey[Boolean](
      "Whether or not the project owner is a group or a user. Defaults to true," +
        " owned by user,"
    )
    val gitlabProjectName = settingKey[String](
      "The GitLab repo name. Defaults to this projects name."
    )

    val gitlabClient =
      settingKey[GitlabClient]("HTTP Client used to communicate with gitlab")

  }

  trait TaskKeys {
    // Different tasks to execute
    val listNamespaces =
      taskKey[Unit]("Shows the namespaces you're associated with.")

    val listProjects =
      taskKey[Unit]("Show the projects for the given namespace.")

    val listPipelines = inputKey[Unit]("Show pipelines for project.")

    val demo = inputKey[Unit]("A demo input task.")
  }

  object autoImport extends SettingKeys with TaskKeys

  import autoImport._

  lazy val gitlabClient =
    Def.setting(GitlabClient(AccessToken(gitlabAuthToken.value)))

  override lazy val projectSettings = {
    Seq(
      gitlabAuthToken := {
        Properties.envOrNone("GITLAB_API_TOKEN").getOrElse {
          sys.error(
            "The Gitlab access token needs to be defined. Please see " +
              s"${gitlabBaseUrl.value}/profile/personal_access_tokens for more" +
              s" details about how to create one."
          )
        }
      },
      gitlabBaseUrl := {
        val scheme = if (gitlabUseTLS.value) "https" else "http"
        s"$scheme://${gitlabHost.value}"
      },
      gitlabUseTLS := true,
      gitlabHost := "gitlab.com",
      gitlabApiVersion := V4,
      gitlabProjectOwnerIsUser := true,
      gitlabProjectName := name.value,
      listNamespaces := {
        implicit val client = gitlabClient.value
        println(
          UsersAndGroups
            .listGroups(gitlabBaseUrl.value, gitlabApiVersion.value)
        )
      },
      listProjects := {
        implicit val client = gitlabClient.value

        if (gitlabProjectOwnerIsUser.value) {
          Project.prettyPrint(
            Projects.listForUser(gitlabBaseUrl.value)
          )
        } else {
          val res = UsersAndGroups
            .listGroups(gitlabBaseUrl.value, gitlabApiVersion.value)
            .find(_.path == gitlabProjectNamespace.value)
            .map { n =>
              Projects
                .listForGroup(
                  baseUrl = gitlabBaseUrl.value,
                  namespace = n
                )
                .pretty(Printer.spaces2)
            }
            .getOrElse(
              s"No projects found for ${gitlabProjectNamespace.value}"
            )
          println(res)
        }
      },
      listPipelines := {
        val t               = (token(Space) ~> token(NatBasic, "<gitlab project id>")).parsed
        val pid             = ProjectId(t)
        val url             = gitlabBaseUrl.value
        val v               = gitlabApiVersion.value
        implicit val client = gitlabClient.value

        println(pid)

        val p = Pipelines.list(url, pid, v)

        println(p.mkString("\n", "\n", "\n"))
      }
    )
  }

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()
}
