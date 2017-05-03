package net.scalytica.sbt

import net.scalytica.sbt.api.APIVersions._
import net.scalytica.sbt.api.{
  GitlabClient,
  Pipelines,
  Projects,
  UsersAndGroups
}
import net.scalytica.sbt.models._
import sbt.Keys._
import sbt._
import sbt.complete.DefaultParsers._
import sbt.plugins.JvmPlugin

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

    val listPipelines = inputKey[Unit]("Show pipelines for the project")

    val showPipelineJob = inputKey[Unit]("Show a specific pipeline job")

    val retryPipelineJob = inputKey[Unit]("Retry a specific pipeline")

    val cancelPipelineJob = inputKey[Unit]("Cancel a running pipeline")

    val demo = inputKey[Unit]("A demo input task.")
  }

  object autoImport extends SettingKeys with TaskKeys

  import autoImport._

  lazy val gitlabClient =
    Def.setting(GitlabClient(AccessToken(gitlabAuthToken.value)))

  lazy val gitlabProject: Def.Initialize[Option[GitlabProject]] =
    Def.setting {
      implicit val client = gitlabClient.value

      val p = if (gitlabProjectOwnerIsUser.value) {
        Projects.listForUser(gitlabBaseUrl.value, gitlabApiVersion.value)
      } else {
        UsersAndGroups
          .listGroups(gitlabBaseUrl.value, gitlabApiVersion.value)
          .find(_.path == gitlabProjectNamespace.value)
          .map { n =>
            Projects.listForGroup(
              baseUrl = gitlabBaseUrl.value,
              namespace = n
            )
          }
          .getOrElse(Seq.empty)
      }
      p.find(_.name == gitlabProjectName.value)
    }

  private val idParser = token(Space) ~> token(NatBasic, "<gitlab project id>")

  override lazy val projectSettings = {
    Seq(
      gitlabAuthToken := {
        Properties.envOrNone("GITLAB_API_TOKEN").getOrElse {
          sys.error(
            "The Gitlab access token needs to be defined. Please see " +
              s"${gitlabBaseUrl.value}/profile/personal_access_tokens for more" +
              s" details about how to create one. Then set the " +
              s"${gitlabAuthToken.key.label} or the 'GITLAB_API_TOKEN' system " +
              s"environment property"
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
      listPipelines := {
        val log             = streams.value.log
        implicit val client = gitlabClient.value

        val project = gitlabProject.value
        val url     = gitlabBaseUrl.value
        val v       = gitlabApiVersion.value

        project.map { proj =>
          val p = Pipelines.list(url, proj.id, v)
          Pipeline.prettyPrint(p)
        }.getOrElse(
          log.warn(s"Could not find any project ${gitlabProjectName.value}")
        )
      },
      showPipelineJob := {
        val log             = streams.value.log
        implicit val client = gitlabClient.value

        val a       = idParser.parsed
        val pipId   = PipelineId(a)
        val project = gitlabProject.value
        val url     = gitlabBaseUrl.value
        val v       = gitlabApiVersion.value

        project.map { proj =>
          val p = Pipelines.get(url, proj.id, pipId, v)
          PipelineDetails.prettyPrint(p)
        }.getOrElse {
          log.warn(s"Could not find any project ${gitlabProjectName.value}")
        }
      },
      retryPipelineJob := {
        val log             = streams.value.log
        implicit val client = gitlabClient.value

        val a       = idParser.parsed
        val pipId   = PipelineId(a)
        val project = gitlabProject.value
        val url     = gitlabBaseUrl.value
        val v       = gitlabApiVersion.value

        project.map { proj =>
          val p = Pipelines.retry(url, proj.id, pipId, v)
          Pipeline.prettyPrint(Seq(p))
        }.getOrElse {
          log.warn(s"Could not find any project ${gitlabProjectName.value}")
        }
      },
      cancelPipelineJob := {
        val log             = streams.value.log
        implicit val client = gitlabClient.value

        val a       = idParser.parsed
        val pipId   = PipelineId(a)
        val project = gitlabProject.value
        val url     = gitlabBaseUrl.value
        val v       = gitlabApiVersion.value

        project.map { proj =>
          val p = Pipelines.cancel(url, proj.id, pipId, v)
          Pipeline.prettyPrint(Seq(p))
        }.getOrElse {
          log.warn(s"Could not find any project ${gitlabProjectName.value}")
        }
      }
    )
  }

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()
}
