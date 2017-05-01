package net.scalytica.sbt.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class GitlabUser(
    id: UserId,
    name: String,
    state: String,
    username: Username
)

object GitlabUser {
  implicit val decoder: Decoder[GitlabUser] = deriveDecoder
}
