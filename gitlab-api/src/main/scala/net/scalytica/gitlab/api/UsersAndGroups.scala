package net.scalytica.gitlab.api

import net.scalytica.gitlab.api.APIVersions.{APIVersion, V4}
import net.scalytica.gitlab.models.{GitLabUser, Namespace}

object UsersAndGroups {

  def listGroups(
      baseUrl: String,
      apiVersion: APIVersion = V4
  )(
      implicit client: GitLabClient
  ): Vector[Namespace] = {
    val url = Urls.NamespacesUrl(baseUrl, apiVersion)
    client.list[Namespace](url)
  }

  def getUser(
      baseUrl: String,
      namespace: Namespace,
      apiVersion: APIVersion = V4
  )(implicit client: GitLabClient): Option[GitLabUser] = {
    val url = Urls.UserByNameUrl(baseUrl, namespace, apiVersion)
    client.list[GitLabUser](url).headOption
  }

}
