package net.scalytica.sbt.api

import io.circe._
import io.circe.parser._
import net.scalytica.sbt.api.GitlabClient.TokenHeaderKey
import net.scalytica.sbt.models.AccessToken
import org.http4s._
import org.http4s.dsl._
import org.http4s.client._
import org.http4s.circe._
import org.http4s.client.asynchttpclient._

case class GitlabClient(token: AccessToken) {

  private val client = AsyncHttpClient()

  private lazy val tokenHeader = Header(TokenHeaderKey, token.value)

  private def getRequest(url: String) = {
    GET(Uri.unsafeFromString(url)).putHeaders(tokenHeader)
  }

  private def postRequest[T](url: String, body: Option[T])(
      implicit e: Encoder[T]
  ) = {
    POST(Uri.unsafeFromString(url), body.map(e.apply).getOrElse(Json.Null))
      .putHeaders(tokenHeader)
  }

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

  def post[In, Out](url: String, body: Option[In])(
      implicit encoder: Encoder[In],
      decoder: Decoder[Out]
  ): Out = {
    val request = postRequest(url, body)
    client.expect[Out](request)(jsonOf[Out]).unsafePerformSync
  }

}

object GitlabClient {

  val TokenHeaderKey = "PRIVATE-TOKEN"

}
