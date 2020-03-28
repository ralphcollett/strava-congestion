package stravacongestion.domain

import io.circe._, io.circe.generic.semiauto._

case class Segments(segments: Set[Segment]) {

  def merge(other: Segments): Segments = {
    Segments(segments | other.segments)
  }
}

object Segments {

  implicit val segmentsDecoder: Decoder[Segments] = deriveDecoder[Segments]
}
