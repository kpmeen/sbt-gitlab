package net.scalytica.sbt.models

import play.api.libs.json._

/*
  {
    "id": 47,
    "status": "pending",
    "ref": "new-pipeline",
    "sha": "a91957a858320c0e17f3a0eca7cfacbff50ea29a"
  }
 */

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

  private val (cols, idCols, statCols, refCols) = (60, 20, 25, 25)
  private val header =
    s"""--------------------------------------------------------------------------
       ||  Pipeline Id       |  Status                 |  Ref/Branch             |
       |--------------------------------------------------------------------------""".stripMargin

  private val footer =
    s"""--------------------------------------------------------------------------""".stripMargin

  def prettyPrint(pips: Seq[Pipeline]): Unit = {
    val rows = pips.map { p =>
      val idStr   = s"|  ${p.id.value}"
      val statStr = s"  ${p.status.prettyPrint}"
      val refStr  = s"  ${p.ref}"
      val x       = (0 to idCols - idStr.length).map(_ => " ").mkString("") + "|"
      val y = (1 to statCols - (statStr.length - 10))
        .map(_ => " ")
        .mkString("") + "|"
      val z = (1 to refCols - refStr.length).map(_ => " ").mkString("") + "|"
      idStr + x + statStr + y + refStr + z
    }

    val str = header + rows.mkString("\n", "\n", "\n") + footer
    println(str)
  }

}
