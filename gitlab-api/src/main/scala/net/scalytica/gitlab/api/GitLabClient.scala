package net.scalytica.gitlab.api

import net.scalytica.gitlab.api.GitLabClient.TokenHeaderKey
import net.scalytica.gitlab.models.AccessToken
import org.http4s._
import org.http4s.client._
import org.http4s.client.asynchttpclient._
import org.http4s.dsl._
import play.api.libs.json._

import scalaz.concurrent.Task

case class GitLabClient(token: AccessToken) {

  private val client = AsyncHttpClient()

  private val appJsonHeaderValue  = "application/json"
  private lazy val tokenHeader    = Header(TokenHeaderKey, token.value)
  private lazy val acceptHeader   = Header("Accept", appJsonHeaderValue)
  private lazy val containsHeader = Header("Content-Type", appJsonHeaderValue)

  private def getRequest(url: String) = {
    GET(Uri.unsafeFromString(url)).putHeaders(tokenHeader, acceptHeader)
  }

  private def postRequest[T](url: String, body: Option[T])(
      implicit w: Writes[T]
  ) = {
    POST(
      Uri.unsafeFromString(url),
      body.map(Json.toJson[T]).getOrElse(JsNull).toString
    ).putHeaders(tokenHeader, acceptHeader, containsHeader)
  }

  private def postRequestNoBody(url: String) = {
    POST(Uri.unsafeFromString(url))
      .putHeaders(tokenHeader, acceptHeader, containsHeader)
  }

  private def parseResponseBody[T](
      res: Response
  )(implicit r: Reads[T]): Task[T] = {
    res.as[String].map { str =>
      val pjs = Json.parse(str)
      Json.fromJson[T](pjs) match {
        case JsSuccess(t, _) => t
        case JsError(errors) =>
          sys.error(s"Error parsing JSON. ${errors.mkString("\n", "\n", "")}")
      }
    }
  }

  def get[T](url: String)(implicit r: Reads[T]): T = {
    val request = getRequest(url)
    client.fetch[T](request)(parseResponseBody[T]).unsafePerformSync
  }

  def list[T](url: String)(implicit r: Reads[T]): Vector[T] = {
    val request = getRequest(url)
    client
      .fetch[Vector[T]](request)(parseResponseBody[Vector[T]])
      .unsafePerformSync
  }

  def post[In, Out](
      url: String,
      body: Option[In]
  )(implicit w: Writes[In], r: Reads[Out]): Out = {
    val request = postRequest(url, body)
    client.fetch[Out](request)(parseResponseBody[Out]).unsafePerformSync
  }

  def postNoBody[Out](
      url: String
  )(implicit r: Reads[Out]): Out = {
    val request = postRequestNoBody(url)
    client.fetch[Out](request)(parseResponseBody[Out]).unsafePerformSync
  }

}

object GitLabClient {

  val TokenHeaderKey = "PRIVATE-TOKEN"

}
