package stravacongestion

import java.time.{LocalDateTime, ZoneId}

import stravacongestion.domain.{BoundCoords, Leaderboard, StravaError}

import scala.io.Source
import cats.implicits._

object TestApp extends App {

  val accessTokenString = Source.fromResource("AccessToken.txt").getLines().toList.mkString
  val strava = new Strava(accessTokenString)
  // Blackheath
//  val southWest = BoundCoords(51.466543, -0.009707)
//  val northEast = BoundCoords(51.477296, 0.017179)
  // Battersea Park
//  val southWest = BoundCoords(51.474617, -0.167098)
//  val northEast = BoundCoords(51.484032, -0.147969)
  // Richmond Park
//  val southWest = BoundCoords(51.418649, -0.321311)
//  val northEast = BoundCoords(51.464652, -0.231877)
  // Bournemouth Prom
//  val southWest = BoundCoords(50.683937, -1.952402)
//  val northEast = BoundCoords(50.724179, -1.814365)
  // Reading Thames
  val southWest = BoundCoords(51.454342, -0.980854)
  val northEast = BoundCoords(51.468082, -0.924238)
  val result: Either[StravaError, Leaderboard] = for (segments <- strava.segments(southWest, northEast);
                    efforts <- segments.segments.map(segment => strava.leaderboard(segment.id)).toList.sequence)
    yield efforts.reduce(_.merge(_))

  result match {
    case Right(leaderboard) =>
      val times = leaderboard.entries.groupBy(entry => LocalDateTime.ofInstant(entry.startDate, ZoneId.systemDefault()).getHour).mapValues(_.size)
      Range(0, 24).foreach(hour => println(s"$hour,${times.getOrElse(hour, 0)}"))
    case Left(error) => sys.error(error.toString)
  }
}