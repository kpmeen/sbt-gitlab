package net.scalytica.gitlab.api

import net.scalytica.gitlab.api.APIVersions.{APIVersion, V4}
import net.scalytica.gitlab.models.{GitlabUser, Namespace}

object UsersAndGroups {

  def listGroups(
      baseUrl: String,
      apiVersion: APIVersion = V4
  )(
      implicit client: GitlabClient
  ): Vector[Namespace] = {
    val url = Urls.NamespacesUrl(baseUrl, apiVersion)
    client.list[Namespace](url)
  }

  def getUser(
      baseUrl: String,
      namespace: Namespace,
      apiVersion: APIVersion = V4
  )(implicit client: GitlabClient): Option[GitlabUser] = {
    val url = Urls.UserByNameUrl(baseUrl, namespace, apiVersion)
    client.list[GitlabUser](url).headOption
  }

}
