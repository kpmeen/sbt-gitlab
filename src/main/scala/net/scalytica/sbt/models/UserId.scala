package net.scalytica.sbt.models

import io.circe.Decoder

case class UserId(value: Int) extends AnyVal

object UserId {
  implicit val decoder: Decoder[UserId] = Decoder.decodeInt.map(UserId.apply)
}
