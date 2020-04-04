package stravacongestion.domain

import java.time.Instant

import io.circe.{Decoder, HCursor}

import scala.util.Try

case class LeadboardEntry(startDate: Instant)

object LeadboardEntry {

  implicit val decodeInstant: Decoder[Instant] = Decoder.decodeString.emapTry { str =>
    Try(Instant.parse(str))
  }

  implicit val segmentDecoder: Decoder[LeadboardEntry] = (c: HCursor) => for {
    startDate <- c.downField("start_date_local").as[Instant]
  } yield LeadboardEntry(startDate)
}