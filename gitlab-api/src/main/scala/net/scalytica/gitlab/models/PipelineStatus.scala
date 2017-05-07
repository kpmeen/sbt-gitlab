package net.scalytica.gitlab.models

import fansi.Color._
import play.api.libs.json._

sealed trait PipelineStatus {

  def prettyPrint: String

  override def toString = prettyPrint

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
  override def prettyPrint = Green(PipelineStatus.SuccessStr).render

  override def toString = prettyPrint
}

case object Pending extends PipelineStatus {
  override def prettyPrint = Yellow(PipelineStatus.PendingStr).render

  override def toString = prettyPrint
}

case object Running extends PipelineStatus {
  override def prettyPrint = LightBlue(PipelineStatus.RunningStr).render

  override def toString = prettyPrint
}

case object Canceled extends PipelineStatus {
  override def prettyPrint = DarkGray(PipelineStatus.CanceledStr).render

  override def toString = prettyPrint
}

case object Failed extends PipelineStatus {
  override def prettyPrint = Red(PipelineStatus.FailedStr).render

  override def toString = prettyPrint
}

case class Unknown(value: String) extends PipelineStatus {
  override def prettyPrint = value

  override def toString = prettyPrint
}
