package net.scalytica.gitlab.models.pipeline

import net.scalytica.gitlab.utils.TablePrinter
import play.api.libs.json._

/**
 * @see [[https://gitlab.com/help/api/pipelines.md#list-project-pipelines]]
 */
case class Pipeline(
    id: PipelineId,
    sha: String,
    ref: String,
    status: PipelineStatus
)

object Pipeline {
  implicit val decoder: Reads[Pipeline] = Json.reads[Pipeline]

  private val headers = Seq("Pipeline Id", "Status", "Ref/Branch")

  def prettyPrint(pip: Pipeline): Unit = prettyPrint(Seq(pip))

  def prettyPrint(pips: Seq[Pipeline]): Unit = {
    val tab = TablePrinter.format(Seq(headers) ++ pips.map { p =>
      Seq(p.id.value, p.status.prettyPrint, p.ref)
    })
    println(tab)
  }

}
