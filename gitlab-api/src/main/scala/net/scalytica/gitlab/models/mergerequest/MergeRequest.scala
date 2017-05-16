package net.scalytica.gitlab.models.mergerequest

import net.scalytica.gitlab.models.{Author, Branch, CommitSha, ProjectId}
import net.scalytica.gitlab.utils.TablePrinter
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * A simplified representation of a Merge Request as returned by the GitLab API.
 * Some attributes have been left out. Mainly to fit inside the 22 attribute
 * limit on Scala tuples.
 */
case class MergeRequest(
    id: Int,
    iid: MergeRequestId,
    projectId: ProjectId,
    title: String,
    description: Option[String],
    state: MergeState,
    targetBranch: Branch,
    targetProjectId: ProjectId,
    sourceBranch: Branch,
    sourceProjectId: ProjectId,
    author: Author,
    assignee: Option[Author],
    labels: Seq[String],
    workInProgress: Boolean,
    mergeWhenPipelineSucceeds: Boolean,
    mergeStatus: Option[MergeStatus],
    sha: CommitSha,
    mergeCommitSha: Option[CommitSha],
    userNotesCount: Int,
    webUrl: String
)

object MergeRequest {

  implicit val reads: Reads[MergeRequest] = (
    (__ \ "id").read[Int] and
      (__ \ "iid").read[MergeRequestId] and
      (__ \ "project_id").read[ProjectId] and
      (__ \ "title").read[String] and
      (__ \ "description").readNullable[String] and
      (__ \ "state").read[MergeState] and
      (__ \ "target_branch").read[Branch] and
      (__ \ "target_project_id").read[ProjectId] and
      (__ \ "source_branch").read[Branch] and
      (__ \ "source_project_id").read[ProjectId] and
      (__ \ "author").read[Author] and
      (__ \ "assignee").readNullable[Author] and
      (__ \ "labels").read[Seq[String]] and
      (__ \ "work_in_progress").read[Boolean] and
      (__ \ "merge_when_pipeline_succeeds").read[Boolean] and
      (__ \ "merge_status").readNullable[MergeStatus] and
      (__ \ "sha").read[CommitSha] and
      (__ \ "merge_commit_sha").readNullable[CommitSha] and
      (__ \ "user_notes_count").read[Int] and
      (__ \ "web_url").read[String]
  )(MergeRequest.apply _)

  private val headers = Seq(
    "iid",
    "title",
    "state",
    "target branch",
    "source branch",
    "author",
    "assignee",
    "labels",
    "WIP",
    "merge on success",
    "merge status",
    "comments"
  )

  private val hf =
    s"""----------------------------------------------------""".stripMargin

  def prettyPrint(mr: MergeRequest): Unit = {
    val branchesTable = TablePrinter.format(
      Seq(
        Seq("", "branch", "projectId"),
        Seq("from", mr.sourceBranch, mr.sourceProjectId),
        Seq("to", mr.targetBranch, mr.targetProjectId)
      )
    )
    val out =
      s"""
         !$hf
         !id              : ${mr.id}
         !iid             : ${mr.iid}
         !project id      : ${mr.projectId}
         !title           : ${mr.title}
         !description     : ${mr.description.getOrElse("")}
         !state           : ${mr.state.value}
         !author          : ${mr.author.user.username}
         !assignee        : ${mr.assignee.map(_.user.username).getOrElse("")}
         !labels          : ${mr.labels.mkString(", ")}
         !WIP             : ${mr.workInProgress}
         !merge on success: ${mr.mergeWhenPipelineSucceeds}
         !merge status    : ${mr.mergeStatus.getOrElse("")}
         !number of notes : ${mr.userNotesCount}
         !branches
         !$branchesTable
         !$hf
       """
      // Need to use a different margin character because of the ASCII table
        .stripMargin('!')

    println(out)
  }

  def prettyPrint(mrs: Seq[MergeRequest]): Unit = {
    val table = TablePrinter.format(Seq(headers) ++ mrs.map { mr =>
      Seq(
        mr.iid,
        mr.title,
        mr.state,
        mr.targetBranch,
        mr.sourceBranch,
        mr.author.user.username.value,
        mr.assignee.map(_.user.username.value).getOrElse(""),
        mr.labels.mkString(", "),
        mr.workInProgress,
        mr.mergeWhenPipelineSucceeds,
        mr.mergeStatus.getOrElse(""),
        mr.userNotesCount
      )
    })
    println(table)
  }

}
