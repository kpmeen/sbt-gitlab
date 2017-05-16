package net.scalytica.gitlab.models.note

import net.scalytica.gitlab.models.note.NoteableTypes.Noteable
import net.scalytica.gitlab.models.{Author, Timestamp}
import net.scalytica.gitlab.utils.TablePrinter
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Note(
    id: NoteId,
    body: String,
    // attachment ???
    author: Author,
    createdAt: Timestamp,
    updatedAt: Option[Timestamp],
    system: Boolean,
    noteableId: Int,
    noteableType: Noteable
)

object Note {

  implicit val reads: Reads[Note] = (
    (__ \ "id").read[NoteId] and
      (__ \ "body").read[String] and
      (__ \ "author").read[Author] and
      (__ \ "created_at").read[Timestamp] and
      (__ \ "updated_at").readNullable[Timestamp] and
      (__ \ "system").read[Boolean] and
      (__ \ "noteable_id").read[Int] and
      (__ \ "noteable_type").read[Noteable]
  )(Note.apply _)

  private val headers = Seq(
    "id",
    "body",
    "author",
    "created",
    "updated",
    "by system",
    "for id",
    "for type"
  )

  def prettyPrint(notes: Seq[Note]): Unit = {
    val table = TablePrinter.format(
      Seq(headers) ++ notes.map { n =>
        Seq(
          n.id,
          n.body.take(20) + "...",
          n.author.user.username,
          n.createdAt,
          n.updatedAt.getOrElse(""),
          n.system,
          n.noteableId,
          n.noteableType
        )
      }
    )
    println(table)
  }

}
