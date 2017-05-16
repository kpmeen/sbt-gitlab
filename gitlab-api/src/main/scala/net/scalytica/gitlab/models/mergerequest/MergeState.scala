package net.scalytica.gitlab.models.mergerequest

import play.api.libs.json._

sealed trait MergeState {
  val value: String
}

object MergeState {

  val All = Seq(NoState, Merged, Opened, Closed)

  implicit val reads: Reads[MergeState] = Reads { jsv =>
    fromString(jsv.as[String])
      .map(ms => JsSuccess(ms))
      .getOrElse(JsError(JsPath, "Invalid"))
  }

  def fromString(str: String): Option[MergeState] =
    All.find(_.value.toLowerCase == str.toLowerCase())

  def unsafeFromString(str: String): MergeState = fromString(str).get

}

case object NoState extends MergeState {
  val value = "all"
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
