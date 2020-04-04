package stravacongestion.domain

import io.circe.Decoder

case class SegmentId(value: Int)

object SegmentId {

  implicit val resourceStateDecoder: Decoder[SegmentId] = Decoder.decodeInt.map(SegmentId(_))
}