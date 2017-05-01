package net.scalytica.sbt.api

import cats.syntax.either._
import io.circe._
import io.circe.parser._
import net.scalytica.sbt.api.GitlabClient.TokenHeaderKey
import net.scalytica.sbt.models.AccessToken
import org.http4s._
import org.http4s.circe._
import org.http4s.client.asynchttpclient._

case class GitlabClient(token: AccessToken) {

  private val client = AsyncHttpClient()

  private lazy val headers = Headers(Header(TokenHeaderKey, token.value))

  private lazy val getRequest = (url: String) =>
    Request(
      method = Method.GET,
      headers = headers,
      uri = Uri.unsafeFromString(url)
  )

  def get[T](url: String)(implicit decoder: Decoder[T]): T = {
    val request = getRequest(url)
    client.expect[T](request)(jsonOf[T]).unsafePerformSync
  }

  def list[T](url: String)(implicit decoder: Decoder[T]): Vector[T] = {
    val request = getRequest(url)
    client
      .fetch[Vector[T]](request) { response =>
        response.as[String].map(s => parse(s)).map {
          case Left(parsingFailure) =>
            sys.error(parsingFailure.message)

          case Right(js) =>
            js.as[Vector[T]] match {
              case Left(decodingFailure) => sys.error(decodingFailure.message)
              case Right(res)            => res
            }
        }
      }
      .unsafePerformSync
  }

}

object GitlabClient {

  val TokenHeaderKey = "PRIVATE-TOKEN"

}
