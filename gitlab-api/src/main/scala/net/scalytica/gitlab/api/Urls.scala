package net.scalytica.gitlab.api

import net.scalytica.gitlab.api.APIVersions.APIVersion
import net.scalytica.gitlab.models.mergerequest.{MergeRequestId, MergeState}
import net.scalytica.gitlab.models.note.NoteId
import net.scalytica.gitlab.models.pipeline.PipelineId
import net.scalytica.gitlab.models.{Namespace, ProjectId}

/*
  Security:
  https://gitlab.com/help/api/README.md#personal-access-tokens

  Fetch info on merge requests...

  GET /projects/:id/merge_requests
  GET /projects/:id/merge_requests?state=opened
  GET /projects/:id/merge_requests?state=all
  GET /projects/:id/merge_requests?iids[]=42&iids[]=43

  GET /projects/:id/merge_requests/:merge_request_iid
  GET /projects/:id/merge_requests/:merge_request_iid/commits
  GET /projects/:id/merge_requests/:merge_request_iid/changes

  https://gitlab.com/help/api/merge_requests.md#create-mr
  https://gitlab.com/help/api/merge_requests.md#update-mr
  https://gitlab.com/help/api/merge_requests.md#delete-a-merge-request
  https://gitlab.com/help/api/merge_requests.md#merge-request-approvals
  https://gitlab.com/help/api/merge_requests.md#approve-merge-request
  https://gitlab.com/help/api/merge_requests.md#unapprove-merge-request
  (https://gitlab.com/help/api/merge_requests.md#cancel-merge-when-pipeline-succeeds)
  https://gitlab.com/help/api/merge_requests.md#comments-on-merge-requests

  https://gitlab.com/help/api/jobs.md
 */

object Urls {

  lazy val BaseUrl = (b: String, v: APIVersion) => s"$b/api/$v"

  lazy val NamespacesUrl = (b: String, v: APIVersion) =>
    s"${BaseUrl(b, v)}/namespaces"

  lazy val GroupProjectsUrl = (b: String, n: Namespace, v: APIVersion) =>
    if (n.isGroup) {
      Some(s"${BaseUrl(b, v)}/${n.kind}s/${n.id}/projects?simple=true")
    } else {
      None
  }

  lazy val UserProjectsUrl = (b: String, v: APIVersion) =>
    s"${BaseUrl(b, v)}/projects?membership=true&simple=true"

  lazy val UserByNameUrl = (b: String, n: Namespace, v: APIVersion) =>
    s"${BaseUrl(b, v)}/users?username=${n.path}"

  lazy val PipelinesUrl = (b: String, p: ProjectId, v: APIVersion) =>
    s"${BaseUrl(b, v)}/projects/$p/pipelines"

  lazy val GetPipelineUrl =
    (b: String, p: ProjectId, pip: PipelineId, v: APIVersion) =>
      s"${BaseUrl(b, v)}/projects/$p/pipelines/$pip"

  lazy val PipelineRetryUrl =
    (b: String, p: ProjectId, pip: PipelineId, v: APIVersion) =>
      s"${BaseUrl(b, v)}/projects/$p/pipelines/$pip/retry"

  lazy val PipelineCancelUrl =
    (b: String, p: ProjectId, pip: PipelineId, v: APIVersion) =>
      s"${BaseUrl(b, v)}/projects/$p/pipelines/$pip/cancel"

  lazy val MergeRequestsUrl =
    (b: String, p: ProjectId, s: MergeState, v: APIVersion) =>
      s"${BaseUrl(b, v)}/projects/$p/merge_requests?state=${s.value}"

  lazy val GetMergeRequestUrl =
    (b: String, p: ProjectId, m: MergeRequestId, v: APIVersion) =>
      s"${BaseUrl(b, v)}/projects/$p/merge_requests/$m"

  lazy val MergeRequestCommentsUrl =
    (b: String, p: ProjectId, m: MergeRequestId, v: APIVersion) =>
      s"${BaseUrl(b, v)}/projects/$p/merge_requests/$m/notes"

  lazy val GetMergeRequestCommentUrl =
    (b: String, p: ProjectId, m: MergeRequestId, n: NoteId, v: APIVersion) =>
      s"${BaseUrl(b, v)}/projects/$p/merge_requests/$m/notes/$n"
}
