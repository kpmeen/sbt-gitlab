package net.scalytica.gitlab.models

import play.api.libs.json._

case class Branch(value: String) extends AnyVal {

  override def toString = value

}

object Branch {

  implicit val reads: Reads[Branch] = __.read[String].map(Branch.apply)

}
