package net.scalytica.gitlab.models

import net.scalytica.gitlab.utils.TablePrinter
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
    pathWithNamespace: String
)

object GitlabProject {

  implicit val reads: Reads[GitlabProject] = (
    (__ \ "id").read[ProjectId] and
      (__ \ "name").read[String] and
      (__ \ "path_with_namespace").read[String]
  )(GitlabProject.apply _)

  private val headers = Seq("project id", "project name")

  def prettyPrint(projs: Seq[GitlabProject]): Unit = {
    val t = TablePrinter.format(
      Seq(headers) ++ projs.map(p => Seq(p.id, p.name))
    )
    println(t)
  }
}
