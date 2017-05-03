package net.scalytica.sbt.models

import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.duration.FiniteDuration

case class PipelineDetails(
    id: PipelineId,
    status: String,
    ref: String,
    tag: Boolean,
    user: GitlabUser,
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
      (__ \ "status").read[String] and
      (__ \ "ref").read[String] and
      (__ \ "tag").read[Boolean] and
      (__ \ "user").read[GitlabUser] and
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

  def prettyPrint(p: PipelineDetails): Unit = {
    println(p)
  }
}
