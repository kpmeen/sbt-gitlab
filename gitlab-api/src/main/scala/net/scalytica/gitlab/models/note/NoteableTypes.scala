package net.scalytica.gitlab.models.note

import play.api.libs.json.{JsSuccess, Reads}

object NoteableTypes {

  sealed trait Noteable {
    val value: String
  }

  object Noteable {

    implicit val reads: Reads[Noteable] = Reads { jsv =>
      jsv.as[String] match {
        case MergeRequest.value => JsSuccess(MergeRequest)
        case Issue.value        => JsSuccess(Issue)
        case other              => JsSuccess(Unknown(other))
      }
    }

  }

  case object MergeRequest extends Noteable {
    val value = "MergeRequest"
  }

  case object Issue extends Noteable {
    val value = "Issue"
  }

  case class Unknown(value: String) extends Noteable

}
