package net.scalytica.gitlab.models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class GitLabUser(
    id: UserId,
    username: Username,
    email: Option[Email],
    name: Option[String],
    state: Option[String],
    webUrl: Option[String]
)

object GitLabUser {

  implicit val reads: Reads[GitLabUser] = (
    (__ \ "id").read[UserId] and
      (__ \ "username").read[Username] and
      (__ \ "email").readNullable[Email] and
      (__ \ "name").readNullable[String] and
      (__ \ "state").readNullable[String] and
      (__ \ "web_url").readNullable[String]
  )(GitLabUser.apply _)

}
