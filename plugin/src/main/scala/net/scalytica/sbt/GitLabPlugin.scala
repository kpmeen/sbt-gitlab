package net.scalytica.sbt

import net.scalytica.gitlab.api.APIVersions._
import net.scalytica.gitlab.api._
import net.scalytica.gitlab.models._
import net.scalytica.gitlab.models.mergerequest._
import net.scalytica.gitlab.models.note.{Note, NoteId}
import net.scalytica.gitlab.models.pipeline.{
  Pipeline,
  PipelineDetails,
  PipelineId
}
import sbt.Keys._
import sbt._
import sbt.complete.DefaultParsers._
import sbt.plugins.JvmPlugin

import scala.util.Properties

object GitLabPlugin extends AutoPlugin {

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
    val gitlabBaseUrl = settingKey[String]("The base URL for the GitLab API.")
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
      settingKey[GitLabClient]("HTTP Client used to communicate with GitLab")

  }

  trait TaskKeys {

    val listPipelines     = taskKey[Unit]("Show test for the project")
    val showPipeline      = inputKey[Unit]("Show a specific pipeline job")
    val retryPipeline     = inputKey[Unit]("Retry a specific pipeline")
    val cancelPipeline    = inputKey[Unit]("Cancel a running pipeline")
    val listMergeRequests = inputKey[Unit]("List merge requests")
    val showMergeRequest  = inputKey[Unit]("Show a specific merge request")
    val listMergeRequestNotes =
      inputKey[Unit]("Show notes/comments for a specific merge request")
    val showMergeRequestNote =
      inputKey[Unit]("Show a specific merge request note")

  }

  object autoImport extends SettingKeys with TaskKeys

  import autoImport._

  lazy val gitlabClient =
    Def.setting(GitLabClient(AccessToken(gitlabAuthToken.value)))

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

  private val mrStateParser =
    token(Space) ~> token(
      StringBasic,
      s"<${MergeState.All.map(_.value).mkString(" | ")}>"
    ).map(MergeState.unsafeFromString)

  private val projectNotFound = (namespace: String) =>
    (name: String) =>
      s"Could not find any project/repository called $namespace / $name"

  override lazy val projectSettings = {
    Seq(
      gitlabAuthToken := {
        Properties.envOrNone("GITLAB_API_TOKEN").getOrElse {
          sys.error(
            "The GitLab access token needs to be defined. Please see " +
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

        val res =
          project.map(p => Right(Pipelines.list(url, p.id, v))).getOrElse {
            Left(
              projectNotFound(gitlabProjectNamespace.value)(
                gitlabProjectName.value
              )
            )
          }

        res match {
          case Right(pipelines) => Pipeline.prettyPrint(pipelines)
          case Left(err)        => log.warn(err)
        }
      },
      showPipeline := {
        val log             = streams.value.log
        implicit val client = gitlabClient.value

        val a       = idParser.parsed
        val pipId   = PipelineId(a)
        val project = gitlabProject.value
        val url     = gitlabBaseUrl.value
        val v       = gitlabApiVersion.value

        val res = project
          .map(p => Right(Pipelines.get(url, p.id, pipId, v)))
          .getOrElse {
            Left(
              projectNotFound(gitlabProjectNamespace.value)(
                gitlabProjectName.value
              )
            )
          }

        res match {
          case Right(pipelines) => PipelineDetails.prettyPrint(pipelines)
          case Left(err)        => log.warn(err)
        }
      },
      retryPipeline := {
        val log             = streams.value.log
        implicit val client = gitlabClient.value

        val a       = idParser.parsed
        val pipId   = PipelineId(a)
        val project = gitlabProject.value
        val url     = gitlabBaseUrl.value
        val v       = gitlabApiVersion.value

        val res = project
          .map(p => Right(Pipelines.retry(url, p.id, pipId, v)))
          .getOrElse {
            Left(
              projectNotFound(gitlabProjectNamespace.value)(
                gitlabProjectName.value
              )
            )
          }

        res match {
          case Right(pipeline) => Pipeline.prettyPrint(pipeline)
          case Left(err)       => log.warn(err)
        }
      },
      cancelPipeline := {
        val log             = streams.value.log
        implicit val client = gitlabClient.value

        val id      = idParser.parsed
        val pipId   = PipelineId(id)
        val project = gitlabProject.value
        val url     = gitlabBaseUrl.value
        val v       = gitlabApiVersion.value

        val res = project
          .map(p => Right(Pipelines.cancel(url, p.id, pipId, v)))
          .getOrElse {
            Left(
              projectNotFound(gitlabProjectNamespace.value)(
                gitlabProjectName.value
              )
            )
          }

        res match {
          case Right(pipeline) => Pipeline.prettyPrint(pipeline)
          case Left(err)       => log.warn(err)
        }
      },
      listMergeRequests := {
        val log             = streams.value.log
        implicit val client = gitlabClient.value

        val state   = mrStateParser.??(NoState).parsed
        val project = gitlabProject.value
        val url     = gitlabBaseUrl.value
        val v       = gitlabApiVersion.value

        val res =
          project
            .map(p => Right(MergeRequests.list(url, p.id, v, state)))
            .getOrElse {
              Left(
                projectNotFound(gitlabProjectNamespace.value)(
                  gitlabProjectName.value
                )
              )
            }

        res match {
          case Right(mergeRequests) => MergeRequest.prettyPrint(mergeRequests)
          case Left(err)            => log.warn(err)
        }
      },
      showMergeRequest := {
        val log             = streams.value.log
        implicit val client = gitlabClient.value

        val id             = idParser.parsed
        val mergeRequestId = MergeRequestId(id)
        val project        = gitlabProject.value
        val url            = gitlabBaseUrl.value
        val v              = gitlabApiVersion.value

        val res =
          project
            .map(p => Right(MergeRequests.get(url, p.id, mergeRequestId, v)))
            .getOrElse {
              Left(
                projectNotFound(gitlabProjectNamespace.value)(
                  gitlabProjectName.value
                )
              )
            }

        res match {
          case Right(mergeRequests) => MergeRequest.prettyPrint(mergeRequests)
          case Left(err)            => log.warn(err)
        }
      },
      listMergeRequestNotes := {
        val log             = streams.value.log
        implicit val client = gitlabClient.value

        val id             = idParser.parsed
        val mergeRequestId = MergeRequestId(id)
        val project        = gitlabProject.value
        val url            = gitlabBaseUrl.value
        val v              = gitlabApiVersion.value

        val res =
          project
            .map(p => Right(MergeRequests.notes(url, p.id, mergeRequestId, v)))
            .getOrElse {
              Left(
                projectNotFound(gitlabProjectNamespace.value)(
                  gitlabProjectName.value
                )
              )
            }

        res match {
          case Right(notes) => Note.prettyPrint(notes)
          case Left(err)    => log.warn(err)
        }
      },
      showMergeRequestNote := {
        val log             = streams.value.log
        implicit val client = gitlabClient.value

        val (mid, nid)     = (idParser ~ idParser).parsed
        val mergeRequestId = MergeRequestId(mid)
        val noteId         = NoteId(nid)
        val project        = gitlabProject.value
        val url            = gitlabBaseUrl.value
        val v              = gitlabApiVersion.value

        val res =
          project.map { p =>
            Right(MergeRequests.note(url, p.id, mergeRequestId, noteId, v))
          }.getOrElse {
            Left(
              projectNotFound(gitlabProjectNamespace.value)(
                gitlabProjectName.value
              )
            )
          }

        res match {
          case Right(note) => Note.prettyPrint(note)
          case Left(err)   => log.warn(err)
        }
      }
    )
  }

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()
}
