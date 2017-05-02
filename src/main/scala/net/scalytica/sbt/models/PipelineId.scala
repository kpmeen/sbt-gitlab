package net.scalytica.sbt.models

import io.circe.Decoder

case class PipelineId(value: Int) extends AnyVal

case object PipelineId {
  implicit val decoder: Decoder[PipelineId] =
    Decoder.decodeInt.map(PipelineId.apply)
}
