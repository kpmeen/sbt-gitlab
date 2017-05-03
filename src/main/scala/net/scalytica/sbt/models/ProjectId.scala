package net.scalytica.sbt.models

import play.api.libs.json._

case class ProjectId(value: Int) extends AnyVal

object ProjectId {
  implicit val decoder: Reads[ProjectId] = __.read[Int].map(ProjectId.apply)
}
