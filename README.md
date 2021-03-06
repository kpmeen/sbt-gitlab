[![build status](https://gitlab.com/kpmeen/sbt-gitlab/badges/master/build.svg)](https://gitlab.com/kpmeen/sbt-gitlab/commits/master)
[ ![Download](https://api.bintray.com/packages/kpmeen/sbt-plugins/sbt-gitlab/images/download.svg) ](https://bintray.com/kpmeen/sbt-plugins/sbt-gitlab/_latestVersion)

# sbt-gitlab

The main goal for this plugin is to allow users to interact with GitLab through
their API's. All from the comfort of your favorite build tool.


## Usage

For the time being it's necessary with a couple of additional steps to be able
to use the plugin. First of all you need to add the following lines to your
`project/plugins.sbt` file:

```scala
resolvers += Resolver.bintrayIvyRepo("kpmeen", "sbt-plugins")
resolvers += Resolver.bintrayRepo("kpmeen", "maven")

addSbtPlugin("net.scalytica" % """sbt-gitlab""" % "0.1.3")
```

## Configuration

The following keys are available to configure the plugin:

```scala
// Settings keys to configure the plugin
val gitlabAuthToken = settingKey[String]("Personal GitLab access token.")
val gitlabUseTLS = settingKey[Boolean]("Whether or not to use HTTP or HTTPS when communicating with the GitLab API.")
val gitlabHost = settingKey[String]("The hostname for the GitLab instance. Defaults to gitlab.com.")
val gitlabApiVersion = settingKey[APIVersion]("The API version to use. Defaults to v4.")
val gitlabBaseUrl = settingKey[String]("The base URL for the GitLab API.")
val gitlabProjectNamespace = settingKey[String]("The GitLab namespace for this project. The namespace is typically the name of the repos owner.")
val gitlabProjectOwnerIsUser = settingKey[Boolean]("Whether or not the project owner is a group or a user. Defaults to true, owned by user.")
val gitlabProjectName = settingKey[String]("The GitLab repo name. Defaults to this projects name.")
```

## Tasks

#### Pipelines

List executed pipelines for the configured project

```scala
listPipelines
```

Retry a specific pipeline

```scala
retryPipeline <pipeline ID>
```

Show a specific pipeline

```scala
showPipeline  <pipeline ID>
```

Cancel an pipeline that is `pending` / `running`

```scala
cancelPipeline  <pipeline ID>
```

#### Merge Requests

List merge requests for the configured project. It's possible to provide a filter for merge request status.

```scala
// list all MR's
listMergeRequests 

// list all opened MR's
listMergeRequests opened

// list all closed MR's
listMergeRequests closed

// list all merged MR's
listMergeRequests merged
```

Show a specific merge request

```scala
showMergeRequest <merge request IID>
```

Show notes/comments for a specific merge request

```scala
listMergeRequestNotes <merge request IID>
```

Show a specific merge request note

```scala
showMergeRequestNote <merge request IID> <note ID>
```