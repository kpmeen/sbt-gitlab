package net.scalytica.sbt.api

import net.scalytica.sbt.api.APIVersions.{APIVersion, V4}
import net.scalytica.sbt.models.{
  Pipeline,
  PipelineDetails,
  PipelineId,
  ProjectId
}

object Pipelines {

  def list(
      baseUrl: String,
      projectId: ProjectId,
      apiVersion: APIVersion = V4
  )(
      implicit client: GitlabClient
  ): Vector[Pipeline] = {
    client.list[Pipeline](Urls.PipelinesUrl(baseUrl, projectId, apiVersion))
  }

  def get(
      baseUrl: String,
      projectId: ProjectId,
      pipeLineId: PipelineId,
      apiVersion: APIVersion
  )(
      implicit client: GitlabClient
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
      implicit client: GitlabClient
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
      implicit client: GitlabClient
  ): Pipeline = {
    client.postNoBody[Pipeline](
      Urls.PipelineCancelUrl(baseUrl, projectId, pipeLineId, apiVersion)
    )
  }

}
