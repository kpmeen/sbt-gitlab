package net.scalytica.gitlab.api

import net.scalytica.gitlab.api.APIVersions.{APIVersion, V4}
import net.scalytica.gitlab.models.{GitLabProject, Namespace}

object Projects {

  def listForGroup(
      baseUrl: String,
      namespace: Namespace,
      apiVersion: APIVersion = V4
  )(implicit client: GitLabClient) = {
    Urls
      .GroupProjectsUrl(baseUrl, namespace, apiVersion)
      .map(url => client.list[GitLabProject](url))
      .getOrElse(Seq.empty)
  }

  def listForUser(
      baseUrl: String,
      apiVersion: APIVersion = V4
  )(implicit client: GitLabClient): Vector[GitLabProject] = {
    client.list[GitLabProject](Urls.UserProjectsUrl(baseUrl, apiVersion))
  }

}
