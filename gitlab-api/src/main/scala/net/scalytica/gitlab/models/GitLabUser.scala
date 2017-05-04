package net.scalytica.gitlab.models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class GitLabUser(
    id: UserId,
    name: String,
    state: String,
    username: Username,
    webUrl: String
)

object GitLabUser {

  implicit val decoder: Reads[GitLabUser] = (
    (__ \ "id").read[UserId] and
      (__ \ "name").read[String] and
      (__ \ "state").read[String] and
      (__ \ "username").read[Username] and
      (__ \ "web_url").read[String]
  )(GitLabUser.apply _)

}
