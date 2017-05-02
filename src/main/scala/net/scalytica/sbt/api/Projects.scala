package net.scalytica.sbt.api

import net.scalytica.sbt.api.APIVersions.{APIVersion, V4}
import net.scalytica.sbt.models.{GitlabProject, Namespace}

object Projects {

  def listForGroup(
      baseUrl: String,
      namespace: Namespace,
      apiVersion: APIVersion = V4
  )(implicit client: GitlabClient) = {
    Urls
      .GroupProjectsUrl(baseUrl, namespace, apiVersion)
      .map(url => client.list[GitlabProject](url))
      .getOrElse(Seq.empty)
  }

  def listForUser(
      baseUrl: String,
      apiVersion: APIVersion = V4
  )(implicit client: GitlabClient): Vector[GitlabProject] = {
    client.list[GitlabProject](Urls.UserProjectsUrl(baseUrl, apiVersion))
  }

}
