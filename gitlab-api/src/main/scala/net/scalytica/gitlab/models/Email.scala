package net.scalytica.gitlab.models

import play.api.libs.json._
import play.api.libs.json.Reads._

case class Email(value: String) extends AnyVal {

  override def toString = value

}

object Email {

  implicit val reads: Reads[Email] =
    __.read[String](email).map(s => Email(s.toLowerCase))

}
