package net.scalytica.sbt.models

import io.circe._
import io.circe.generic.semiauto._

/*
  {
    "id": 47,
    "status": "pending",
    "ref": "new-pipeline",
    "sha": "a91957a858320c0e17f3a0eca7cfacbff50ea29a"
  }
 */

/**
 * @see [[https://gitlab.com/help/api/pipelines.md#list-project-pipelines]]
 */
case class Pipeline(
    id: Int,
    status: String,
    ref: String,
    sha: String
)

object Pipeline {
  implicit val decoder: Decoder[Pipeline] = deriveDecoder
}
