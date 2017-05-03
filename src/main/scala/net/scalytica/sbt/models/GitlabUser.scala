package net.scalytica.sbt.models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class GitlabUser(
    id: UserId,
    name: String,
    state: String,
    username: Username,
    webUrl: String
)

object GitlabUser {

  implicit val decoder: Reads[GitlabUser] = (
    (__ \ "id").read[UserId] and
      (__ \ "name").read[String] and
      (__ \ "state").read[String] and
      (__ \ "username").read[Username] and
      (__ \ "web_url").read[String]
  )(GitlabUser.apply _)

}
