package net.scalytica.gitlab.utils

import org.slf4j.LoggerFactory

import scala.util.Try
import scala.util.control.NonFatal

object TablePrinter {

  val logger = LoggerFactory.getLogger(TablePrinter.getClass)

  def format(t: Seq[Seq[Any]]) =
    Try {
      t match {
        case Nil => ""
        case _ =>
          val cellSizes =
            t.map(_.map(c => Option(c).fold(0)(_.toString.length)))
          val colSizes = cellSizes.transpose.map(_.max)
          val rows     = t.map(r => formatRow(r, colSizes))

          formatRows(rowSeparator(colSizes), rows)
      }
    }.recover {
      case NonFatal(ex) =>
        logger.error("There was an error formatting to a table string", ex)
        ""
    }.getOrElse("")

  def formatRow(row: Seq[Any], colSizes: Seq[Int]) = {
    val cells = row.zip(colSizes).map {
      case (_, size: Int) if size == 0 => ""
      case (item, size)                => ("%" + size + "s").format(item)

    }
    cells.mkString("|", "|", "|")
  }

  def formatRows(separator: String, rows: Seq[String]): String =
    (separator :: rows.head :: separator :: rows.tail.toList ::: separator :: Nil)
      .mkString("\n")

  private def rowSeparator(colSizes: Seq[Int]) =
    colSizes.map(s => "-" * s).mkString("+", "+", "+")
}
