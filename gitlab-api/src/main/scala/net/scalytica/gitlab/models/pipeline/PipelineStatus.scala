package net.scalytica.gitlab.models.pipeline

import fansi.Color._
import play.api.libs.json._

sealed trait PipelineStatus {

  val color: fansi.EscapeAttr

  def prettyPrint: fansi.Str = color(plainPrint)

  def plainPrint: String

}

object PipelineStatus {

  val SuccessStr  = "success"
  val PendingStr  = "pending"
  val RunningStr  = "running"
  val CanceledStr = "canceled"
  val FailedStr   = "failed"

  implicit val reads: Reads[PipelineStatus] = Reads { js =>
    js.validate[String].map {
      case SuccessStr  => Success
      case PendingStr  => Pending
      case RunningStr  => Running
      case CanceledStr => Canceled
      case FailedStr   => Failed
      case unknown     => Unknown(unknown)
    }
  }

}

case object Success extends PipelineStatus {
  override val color      = Green
  override def plainPrint = PipelineStatus.SuccessStr
  override def toString   = prettyPrint.render
}

case object Pending extends PipelineStatus {
  override val color      = Yellow
  override def plainPrint = PipelineStatus.PendingStr
  override def toString   = prettyPrint.render
}

case object Running extends PipelineStatus {
  override val color      = LightBlue
  override def plainPrint = PipelineStatus.RunningStr
  override def toString   = prettyPrint.render
}

case object Canceled extends PipelineStatus {
  override val color      = DarkGray
  override def plainPrint = PipelineStatus.CanceledStr
  override def toString   = prettyPrint.render
}

case object Failed extends PipelineStatus {
  override val color      = Red
  override def plainPrint = PipelineStatus.FailedStr
  override def toString   = prettyPrint.render
}

case class Unknown(value: String) extends PipelineStatus {
  override val color      = White
  override def plainPrint = value
  override def toString   = prettyPrint.render
}
