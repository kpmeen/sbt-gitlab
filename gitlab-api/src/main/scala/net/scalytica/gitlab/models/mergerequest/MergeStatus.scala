package net.scalytica.gitlab.models.mergerequest

import play.api.libs.json.{JsSuccess, Reads}

sealed trait MergeStatus {
  val in: String
}

object MergeStatus {

  implicit val reads: Reads[MergeStatus] = Reads { jsv =>
    jsv.as[String] match {
      case CanBeMerged.in    => JsSuccess(CanBeMerged)
      case CannotBeMerged.in => JsSuccess(CannotBeMerged)
      case unknown           => JsSuccess(UnknownStatus(unknown))
    }
  }

}

case object CanBeMerged extends MergeStatus {
  val in = "can_be_merged"

  override def toString = "Can be merged"
}

case object CannotBeMerged extends MergeStatus {
  val in = "cannot_be_merged"

  override def toString = "Cannot be merged"
}

case class UnknownStatus(in: String) extends MergeStatus {

  override def toString = in

}
