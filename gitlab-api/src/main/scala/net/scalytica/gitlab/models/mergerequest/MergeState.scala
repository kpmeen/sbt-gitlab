package net.scalytica.gitlab.models.mergerequest

import play.api.libs.json._

sealed trait MergeState {
  val value: String
}

object MergeState {

  val All = Seq(Merged, Opened, Closed)

  implicit val reads: Reads[MergeState] = Reads { jsv =>
    fromString(jsv.as[String])
      .map(ms => JsSuccess(ms))
      .getOrElse(JsError(JsPath, "Invalid"))
  }

  def fromString(str: String): Option[MergeState] = All.find(_.value == str)

  def unsafeFromString(str: String): MergeState = fromString(str).get

}

case object Merged extends MergeState {
  val value = "merged"
}

case object Opened extends MergeState {
  val value = "opened"
}

case object Closed extends MergeState {
  val value = "closed"
}
