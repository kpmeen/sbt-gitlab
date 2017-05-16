package net.scalytica.gitlab.models

import play.api.libs.json._

case class Author(
    user: GitLabUser,
    createdAt: Option[Timestamp]
)

object Author {

  implicit val reads: Reads[Author] = Reads { jsv =>
    for {
      user      <- jsv.validate[GitLabUser]
      createdAt <- (jsv \ "created_at").validateOpt[Timestamp]
    } yield Author(user, createdAt)
  }

}
