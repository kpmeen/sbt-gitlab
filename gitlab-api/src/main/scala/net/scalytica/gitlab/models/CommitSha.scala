package net.scalytica.gitlab.models

import play.api.libs.json._

case class CommitSha(value: String) extends AnyVal {

  override def toString = value

}

object CommitSha {

  implicit val reads: Reads[CommitSha] = __.read[String].map(CommitSha.apply)

}
