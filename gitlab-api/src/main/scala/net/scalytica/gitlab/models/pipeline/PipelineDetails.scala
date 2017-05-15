package net.scalytica.gitlab.models.pipeline

import net.scalytica.gitlab.models.{
  Branch,
  CommitSha,
  GitLabUser,
  Timestamp,
  finiteDurationDecoder
}
import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.concurrent.duration.FiniteDuration

case class PipelineDetails(
    id: PipelineId,
    status: PipelineStatus,
    ref: Branch,
    tag: Boolean,
    user: GitLabUser,
    sha: CommitSha,
    beforeSha: Option[CommitSha],
    yamlErrors: Option[String],
    createdAt: Timestamp,
    updatedAt: Option[Timestamp],
    startedAt: Option[Timestamp],
    finishedAt: Option[Timestamp],
    committedAt: Option[Timestamp],
    duration: Option[FiniteDuration], // in seconds
    coverage: Option[Double]
)

object PipelineDetails {

  implicit val reader: Reads[PipelineDetails] = (
    (__ \ "id").read[PipelineId] and
      (__ \ "status").read[PipelineStatus] and
      (__ \ "ref").read[Branch] and
      (__ \ "tag").read[Boolean] and
      (__ \ "user").read[GitLabUser] and
      (__ \ "sha").read[CommitSha] and
      (__ \ "before_sha").readNullable[CommitSha] and
      (__ \ "yaml_errors").readNullable[String] and
      (__ \ "created_at").read[Timestamp] and
      (__ \ "updated_at").readNullable[Timestamp] and
      (__ \ "started_at").readNullable[Timestamp] and
      (__ \ "finished_at").readNullable[Timestamp] and
      (__ \ "comitted_at").readNullable[Timestamp] and
      (__ \ "duration").readNullable[FiniteDuration] and
      (__ \ "coverage").readNullable[Double]
  )(PipelineDetails.apply _)

  private val hf =
    s"""----------------------------------------------------""".stripMargin

  def prettyPrint(p: PipelineDetails): Unit = {
    val out =
      s"""
         |$hf
         |pipeline id : ${p.id}
         |status      : ${p.status.prettyPrint}
         |ref / branch: ${p.ref}
         |tag         : ${p.tag}
         |username    : ${p.user.username}
         |created at  : ${p.createdAt}
         |updated at  : ${p.updatedAt.getOrElse("-")}
         |started at  : ${p.startedAt.getOrElse("-")}
         |finished at : ${p.finishedAt.getOrElse("-")}
         |committed at: ${p.committedAt.getOrElse("-")}
         |duration    : ${p.duration.getOrElse("-")}
         |coverage    : ${p.coverage.getOrElse("-")}
         |$hf
       """.stripMargin

    println(out)
  }
}
