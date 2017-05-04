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

addSbtPlugin("net.scalytica" % """sbt-gitlab""" % "0.1.2")
```

## Configuration

tbd...

## Tasks

tbd...
