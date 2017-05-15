package net.scalytica.gitlab.models.mergerequest

import net.scalytica.gitlab.models.{Email, Timestamp, UserId, Username}
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Author(
    id: UserId,
    username: Username,
    email: Option[Email],
    name: Option[String],
    state: Option[String],
    createdAt: Option[Timestamp]
)

object Author {

  implicit val reads: Reads[Author] = (
    (__ \ "id").read[UserId] and
      (__ \ "username").read[Username] and
      (__ \ "email").readNullable[Email] and
      (__ \ "name").readNullable[String] and
      (__ \ "state").readNullable[String] and
      (__ \ "createdAt").readNullable[Timestamp]
  )(Author.apply _)

}
