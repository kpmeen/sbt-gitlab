package net.scalytica.gitlab.models

import play.api.libs.json._

case class UserId(value: Int) extends AnyVal

object UserId {
  implicit val decoder: Reads[UserId] = __.read[Int].map(UserId.apply)
}
