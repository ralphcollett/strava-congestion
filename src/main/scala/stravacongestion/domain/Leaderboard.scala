package stravacongestion.domain

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class Leaderboard(entries: Set[LeadboardEntry]) {

  def merge(other: Leaderboard): Leaderboard = this.copy(entries = entries ++ other.entries)
}

object Leaderboard {

  implicit val leaderboardDecoder: Decoder[Leaderboard] = deriveDecoder[Leaderboard]
}