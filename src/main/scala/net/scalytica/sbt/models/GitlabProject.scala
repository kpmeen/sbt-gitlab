package net.scalytica.sbt.models

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Representation of a simple project model.
 *
 * @see [[https://gitlab.com/help/api/projects.md#list-projects]]
 */
case class GitlabProject(
    id: ProjectId,
    name: String,
    path_with_namespace: String
)

object GitlabProject {

  implicit val decoder: Reads[GitlabProject] = (
    (__ \ "id").read[ProjectId] and
      (__ \ "name").read[String] and
      (__ \ "path_with_namespace").read[String]
  )(GitlabProject.apply _)

  private val (cols, idCols, noCols) = (60, 20, 38)
  private val header =
    s"""------------------------------------------------------------
       ||  Project ID        |  Project name                       |
       |------------------------------------------------------------""".stripMargin

  private val footer =
    s"""------------------------------------------------------------""".stripMargin

  def prettyPrint(projs: Seq[GitlabProject]): Unit = {
    val rows = projs.map { p =>
      val idStr = s"|  ${p.id.value}"
      val noStr = s"  ${p.name}"
      val x     = (1 to idCols - idStr.length).map(_ => " ").mkString("") + "|"
      val y     = (1 to noCols - noStr.length).map(_ => " ").mkString("") + "|"
      idStr + x + noStr + y
    }

    val str = header + rows.mkString("\n", "\n", "\n") + footer
    println(str)
  }
}
