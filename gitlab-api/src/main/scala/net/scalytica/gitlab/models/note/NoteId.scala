package net.scalytica.gitlab.models.note

import play.api.libs.json.{Reads, __}

case class NoteId(value: Int) extends AnyVal {

  override def toString = value.toString

}

object NoteId {
  implicit val reads: Reads[NoteId] = __.read[Int].map(NoteId.apply)
}
