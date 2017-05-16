package net.scalytica.gitlab.models

import play.api.libs.json._

case class ProjectId(value: Int) extends AnyVal {

  override def toString = value.toString

}

object ProjectId {
  implicit val reads: Reads[ProjectId] = __.read[Int].map(ProjectId.apply)
}
