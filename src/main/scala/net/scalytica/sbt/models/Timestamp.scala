package net.scalytica.sbt.models

import java.time.format.DateTimeParseException
import java.time.{ZoneId, ZonedDateTime}

import play.api.libs.json._

import scala.util.Try

case class Timestamp(zdt: ZonedDateTime) {

  override def toString: String = zdt.toString

}

object Timestamp {

  implicit val decoder: Reads[Timestamp] = Reads { js =>
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
