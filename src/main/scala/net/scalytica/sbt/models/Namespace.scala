package net.scalytica.sbt.models

import net.scalytica.sbt.models.NamespaceKinds._
import play.api.libs.json._

object NamespaceKinds {

  sealed trait NamespaceKind

  object NamespaceKind {

    private[this] val UserNS  = "user"
    private[this] val GroupNS = "group"

    implicit val decoder: Reads[NamespaceKind] = Reads { js =>
      js.validate[String] match {
        case JsSuccess(v, _) if v == UserNS  => JsSuccess(UserKind)
        case JsSuccess(v, _) if v == GroupNS => JsSuccess(GroupKind)
        case JsSuccess(v, p) =>
          val err = JsonValidationError(s"Not a recognized namespace.kind")
          throw JsResultException(Seq(p -> Seq(err)))

        case err: JsError => err
      }
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
  implicit val decoder: Reads[Namespace] = Json.reads[Namespace]
}
