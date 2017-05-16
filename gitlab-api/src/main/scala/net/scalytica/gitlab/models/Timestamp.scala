package net.scalytica.gitlab.models

import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.time.{ZoneId, ZonedDateTime}

import play.api.libs.json._

import scala.util.Try

case class Timestamp(zdt: ZonedDateTime) {

  override def toString = zdt.toLocalDateTime.format(Timestamp.DateTimeFormat)

}

object Timestamp {

  val DateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  implicit val reads: Reads[Timestamp] = Reads { js =>
    js.validate[String] match {
      case JsSuccess(s, p) =>
        Try {
          val dt =
            ZonedDateTime.parse(s).withZoneSameInstant(ZoneId.systemDefault)
          JsSuccess(Timestamp(dt))
        }.recover {
          case dte: DateTimeParseException =>
            JsError(p, s"Illegal date format found for '$s'")
        }.get

      case err: JsError => err
    }
  }

}
