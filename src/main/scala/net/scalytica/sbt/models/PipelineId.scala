package net.scalytica.sbt.models

import play.api.libs.json._

case class PipelineId(value: Int) extends AnyVal

case object PipelineId {
  implicit val decoder: Reads[PipelineId] = __.read[Int].map(PipelineId.apply)
}
