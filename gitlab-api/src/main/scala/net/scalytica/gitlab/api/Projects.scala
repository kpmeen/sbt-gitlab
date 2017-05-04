package net.scalytica.gitlab.api

import net.scalytica.gitlab.api.APIVersions.{APIVersion, V4}
import net.scalytica.gitlab.models.{GitlabProject, Namespace}

object Projects {

  def listForGroup(
      baseUrl: String,
      namespace: Namespace,
      apiVersion: APIVersion = V4
  )(implicit client: GitLabClient) = {
    Urls
      .GroupProjectsUrl(baseUrl, namespace, apiVersion)
      .map(url => client.list[GitlabProject](url))
      .getOrElse(Seq.empty)
  }

  def listForUser(
      baseUrl: String,
      apiVersion: APIVersion = V4
  )(implicit client: GitLabClient): Vector[GitlabProject] = {
    client.list[GitlabProject](Urls.UserProjectsUrl(baseUrl, apiVersion))
  }

}
