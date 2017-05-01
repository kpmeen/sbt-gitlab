package net.scalytica.sbt.api

import net.scalytica.sbt.api.APIVersions.{APIVersion, V4}
import net.scalytica.sbt.models.{Pipeline, ProjectId}

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

}
