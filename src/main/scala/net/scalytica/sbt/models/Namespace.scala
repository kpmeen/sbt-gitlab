package net.scalytica.sbt.models

import io.circe._
import io.circe.generic.semiauto._
import net.scalytica.sbt.models.NamespaceKinds._

object NamespaceKinds {

  sealed trait NamespaceKind

  object NamespaceKind {

    private[this] val UserNS  = "user"
    private[this] val GroupNS = "group"

    implicit val decoder: Decoder[NamespaceKind] = Decoder.decodeString.emap {
      case UserNS  => Right(UserKind)
      case GroupNS => Right(GroupKind)
      case err     => Left(s"$err is not a recognized namespace.kind")
    }
  }

  case object UserKind extends NamespaceKind

  case object GroupKind extends NamespaceKind

}

case class Namespace(
    id: Int,
    name: String,
    path: String,
    kind: NamespaceKind
) {

  lazy val isGroup = kind match {
    case GroupKind => true
    case _         => false
  }

}

object Namespace {
  implicit val decoder: Decoder[Namespace] = deriveDecoder
}
