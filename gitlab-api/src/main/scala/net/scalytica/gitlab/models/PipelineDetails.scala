package net.scalytica.gitlab.models

import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.duration.FiniteDuration

case class PipelineDetails(
    id: PipelineId,
    status: PipelineStatus,
    ref: String,
    tag: Boolean,
    user: GitLabUser,
    sha: String,
    beforeSha: Option[String],
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
      (__ \ "ref").read[String] and
      (__ \ "tag").read[Boolean] and
      (__ \ "user").read[GitLabUser] and
      (__ \ "sha").read[String] and
      (__ \ "before_sha").readNullable[String] and
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
    println(hf)
    println(s"pipeline ID : ${p.id}")
    println(s"status      : ${p.status.prettyPrint}")
    println(s"ref / branch: ${p.ref}")
    println(s"tag         : ${p.tag}")
    println(s"username    : ${p.user.name}")
    println(s"created at  : ${p.createdAt}")
    println(s"updated at  : ${p.updatedAt.getOrElse("-")}")
    println(s"started at  : ${p.startedAt.getOrElse("-")}")
    println(s"finished at : ${p.finishedAt.getOrElse("-")}")
    println(s"committed at: ${p.committedAt.getOrElse("-")}")
    println(s"duration    : ${p.duration.getOrElse("-")}")
    println(s"coverage    : ${p.coverage.getOrElse("-")}")
    println(hf)
  }
}
