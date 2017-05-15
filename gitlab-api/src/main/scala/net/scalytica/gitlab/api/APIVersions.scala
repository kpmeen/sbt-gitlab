package net.scalytica.gitlab.api

object APIVersions {

  sealed trait APIVersion {
    val version: String

    override def toString = version
  }

  case object V4 extends APIVersion {
    override val version = "v4"
  }

}
