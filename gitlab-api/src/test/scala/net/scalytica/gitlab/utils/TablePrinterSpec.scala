package net.scalytica.gitlab.utils

import net.scalytica.gitlab.models.Pipeline
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json._

/**
 * Not really a test as much as it's a means of quickly trying out the
 * table printer code on the console.
 */
class TablePrinterSpec extends WordSpec with MustMatchers {

  val jsString =
    """[
      |  {
      |    "id": 7721678,
      |    "ref": "library-split",
      |    "sha": "01d21a4504afc54c2acda4f0d4cd37ef5ebd8f16",
      |    "status": "canceled"
      |  },
      |  {
      |    "id": 7396546,
      |    "ref": "library-split",
      |    "sha": "1f18616997d9934c3aa7ed8282ec12934cf62238",
      |    "status": "failed"
      |  },
      |  {
      |    "id": 6402880,
      |    "ref": "gitlabci",
      |    "sha": "344342ff6ea445a59404f546975b9946d9eaf1a9",
      |    "status": "success"
      |  },
      |  {
      |    "id": 6402762,
      |    "ref": "gitlabci",
      |    "sha": "344342ff6ea445a59404f546975b9946d9eaf1a9",
      |    "status": "running"
      |  },
      |  {
      |    "id": 6397239,
      |    "ref": "gitlabci",
      |    "sha": "3dd11a6be29ec170afe9629e4e32960c4153cec6",
      |    "status": "pending"
      |  }
      |]
    """.stripMargin

  val json = Json.parse(jsString)
  val pips = Json.fromJson[Seq[Pipeline]](json).get

  "The TablePrinter" should {
    "print a table with headers and rows" in {
      val headers = Seq("Pipeline ID", "Status", "Ref/Branch")
      val rows    = pips.map(p => Seq(p.id.value, p.status.prettyPrint, p.ref))
      val t       = Seq(headers) ++ rows

      val table = TablePrinter.format(t)

      println(table)

      true mustBe true
    }
  }

}
