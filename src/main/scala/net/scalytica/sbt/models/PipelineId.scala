package net.scalytica.sbt.models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class PipelineId(value: Int) extends AnyVal

case object PipelineId {
  implicit val decoder: Decoder[PipelineId] = deriveDecoder
}
