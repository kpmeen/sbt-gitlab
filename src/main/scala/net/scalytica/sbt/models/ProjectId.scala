package net.scalytica.sbt.models

import io.circe.Decoder

case class ProjectId(value: Int) extends AnyVal

object ProjectId {
  implicit val decoder: Decoder[ProjectId] =
    Decoder.decodeInt.map(ProjectId.apply)
}
