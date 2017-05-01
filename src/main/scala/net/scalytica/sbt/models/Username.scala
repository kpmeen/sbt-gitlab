package net.scalytica.sbt.models

import io.circe.Decoder

case class Username(value: String) extends AnyVal

object Username {
  implicit val decoder: Decoder[Username] =
    Decoder.decodeString.map(Username.apply)
}
