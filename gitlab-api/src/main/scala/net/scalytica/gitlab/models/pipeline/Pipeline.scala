package net.scalytica.gitlab.models.pipeline

import net.scalytica.gitlab.models.{Branch, CommitSha}
import net.scalytica.gitlab.utils.TablePrinter
import play.api.libs.json._

/**
 * @see [[https://gitlab.com/help/api/pipelines.md#list-project-pipelines]]
 */
case class Pipeline(
    id: PipelineId,
    sha: CommitSha,
    ref: Branch,
    status: PipelineStatus
)

object Pipeline {
  implicit val decoder: Reads[Pipeline] = Json.reads[Pipeline]

  private val headers = Seq("pipeline id", "status", "ref/branch", "sha")

  def prettyPrint(pip: Pipeline): Unit = prettyPrint(Seq(pip))

  def prettyPrint(pips: Seq[Pipeline]): Unit = {
    val table = TablePrinter.format(Seq(headers) ++ pips.map { p =>
      Seq(p.id, p.status.prettyPrint, p.ref, p.sha)
    })
    println(table)
  }

}
