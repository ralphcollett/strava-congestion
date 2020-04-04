package stravacongestion

import java.time.{LocalDateTime, ZoneId}

import stravacongestion.domain.{BoundCoords, Leaderboard, StravaError}

import scala.io.Source
import cats.implicits._

object TestApp extends App {

  val accessTokenString = Source.fromResource("AccessToken.txt").getLines().toList.mkString
  val strava = new Strava(accessTokenString)
  val southWest = BoundCoords(51.466543, -0.009707)
  val northEast = BoundCoords(51.477296, 0.017179)

  val result: Either[StravaError, Leaderboard] = for (segments <- strava.segments(southWest, northEast);
                    efforts <- segments.segments.map(segment => strava.leaderboard(segment.id)).toList.sequence)
    yield efforts.reduce(_.merge(_))

  result match {
    case Right(leaderboard) =>
      val times = leaderboard.entries.groupBy(entry => LocalDateTime.ofInstant(entry.startDate, ZoneId.systemDefault()).getHour).mapValues(_.size)
      times.toList.sortBy(_._1).foreach { case (hour, runners) => println(s"$hour,$runners") }
    case Left(error) => sys.error(error.toString)
  }
}