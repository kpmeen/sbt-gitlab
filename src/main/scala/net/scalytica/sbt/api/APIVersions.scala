package net.scalytica.sbt.api

object APIVersions {

  sealed trait APIVersion {
    val version: String
  }

  case object V4 extends APIVersion {
    override val version = "v4"
  }

}
