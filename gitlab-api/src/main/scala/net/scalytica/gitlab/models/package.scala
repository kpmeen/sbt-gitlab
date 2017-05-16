package net.scalytica.gitlab

import play.api.libs.json._

import scala.concurrent.duration
import scala.concurrent.duration.FiniteDuration

package object models {

  implicit val finiteDurationReads: Reads[FiniteDuration] =
    __.read[Long].map(i => FiniteDuration(i, duration.SECONDS))

}
