package net.scalytica.gitlab.models

import play.api.libs.json._

case class Username(value: String) extends AnyVal {

  override def toString = value

}

object Username {
  implicit val decoder: Reads[Username] = __.read[String].map(Username.apply)
}
