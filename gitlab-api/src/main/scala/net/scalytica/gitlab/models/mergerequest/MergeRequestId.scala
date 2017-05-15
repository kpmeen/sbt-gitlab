package net.scalytica.gitlab.models.mergerequest

import play.api.libs.json._

case class MergeRequestId(value: Int) extends AnyVal {

  override def toString = value.toString

}

case object MergeRequestId {
  implicit val decoder: Reads[MergeRequestId] =
    __.read[Int].map(MergeRequestId.apply)
}
