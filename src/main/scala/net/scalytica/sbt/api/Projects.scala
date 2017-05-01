package net.scalytica.sbt.api

import io.circe.Json
import net.scalytica.sbt.api.APIVersions.{APIVersion, V4}
import net.scalytica.sbt.models.{Namespace, Project}

object Projects {

  def listForGroup(
      baseUrl: String,
      namespace: Namespace,
      apiVersion: APIVersion = V4
  )(implicit client: GitlabClient) = {
    Urls
      .GroupProjectsUrl(baseUrl, namespace, apiVersion)
      .map(url => client.get[Json](url))
      .getOrElse(Json.Null)
  }

  def listForUser(
      baseUrl: String,
      apiVersion: APIVersion = V4
  )(implicit client: GitlabClient): Vector[Project] = {
    client.list[Project](Urls.UserProjectsUrl(baseUrl, apiVersion))
  }

}
