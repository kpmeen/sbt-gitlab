package net.scalytica.sbt.models

import play.api.libs.json._
import fansi.Color._
import fansi.Str

sealed trait PipelineStatus {

  def prettyPrint: String

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
}

case object Pending extends PipelineStatus {
  override def prettyPrint = Yellow(PipelineStatus.PendingStr).render
}

case object Running extends PipelineStatus {
  override def prettyPrint = LightBlue(PipelineStatus.RunningStr).render
}

case object Canceled extends PipelineStatus {
  override def prettyPrint = DarkGray(PipelineStatus.CanceledStr).render
}

case object Failed extends PipelineStatus {
  override def prettyPrint = Red(PipelineStatus.FailedStr).render
}

case class Unknown(value: String) extends PipelineStatus {
  override def prettyPrint = value
}
