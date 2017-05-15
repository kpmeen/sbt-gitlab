package net.scalytica.gitlab.api

import net.scalytica.gitlab.api.APIVersions.{APIVersion, V4}
import net.scalytica.gitlab.models.ProjectId
import net.scalytica.gitlab.models.mergerequest.{MergeRequest, MergeRequestId}

object MergeRequests {

  def list(
      baseUrl: String,
      projectId: ProjectId,
      apiVersion: APIVersion = V4
  )(
      implicit client: GitLabClient
  ): Vector[MergeRequest] = {
    client.list[MergeRequest](
      Urls.MergeRequestsUrl(baseUrl, projectId, apiVersion)
    )
  }

  def get(
      baseUrl: String,
      projectId: ProjectId,
      mergeRequestId: MergeRequestId,
      apiVersion: APIVersion
  )(
      implicit client: GitLabClient
  ): MergeRequest = {
    client.get[MergeRequest](
      Urls.GetMergeRequestUrl(baseUrl, projectId, mergeRequestId, apiVersion)
    )
  }

}
