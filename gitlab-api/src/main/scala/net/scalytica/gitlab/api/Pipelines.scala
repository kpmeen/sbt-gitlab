package net.scalytica.gitlab.api

import net.scalytica.gitlab.api.APIVersions.{APIVersion, V4}
import net.scalytica.gitlab.models.pipeline.{
  Pipeline,
  PipelineDetails,
  PipelineId
}
import net.scalytica.gitlab.models.ProjectId

object Pipelines {

  def list(
      baseUrl: String,
      projectId: ProjectId,
      apiVersion: APIVersion = V4
  )(
      implicit client: GitLabClient
  ): Vector[Pipeline] = {
    client.list[Pipeline](Urls.PipelinesUrl(baseUrl, projectId, apiVersion))
  }

  def get(
      baseUrl: String,
      projectId: ProjectId,
      pipeLineId: PipelineId,
      apiVersion: APIVersion
  )(
      implicit client: GitLabClient
  ): PipelineDetails = {
    client.get[PipelineDetails](
      Urls.GetPipelineUrl(baseUrl, projectId, pipeLineId, apiVersion)
    )
  }

  def retry(
      baseUrl: String,
      projectId: ProjectId,
      pipeLineId: PipelineId,
      apiVersion: APIVersion
  )(
      implicit client: GitLabClient
  ): Pipeline = {
    client.postNoBody[Pipeline](
      Urls.PipelineRetryUrl(baseUrl, projectId, pipeLineId, apiVersion)
    )
  }

  def cancel(
      baseUrl: String,
      projectId: ProjectId,
      pipeLineId: PipelineId,
      apiVersion: APIVersion
  )(
      implicit client: GitLabClient
  ): Pipeline = {
    client.postNoBody[Pipeline](
      Urls.PipelineCancelUrl(baseUrl, projectId, pipeLineId, apiVersion)
    )
  }

}
