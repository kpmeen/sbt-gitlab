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

tbd...

## Tasks

List executed pipelines for the configured project 
```
listPipelines
```

Retry a specific pipeline
```
retryPipeline <pipeline ID>
```

Show a specific pipeline
```
showPipeline  <pipeline ID>
```

Cancel an pipeline that is `pending` / `running`
```
cancelPipeline  <pipeline ID>
```
