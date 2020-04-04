package stravacongestion

import stravacongestion.domain.BoundCoords

import scala.io.Source
import cats.implicits._

object TestApp extends App {

  val accessTokenString = Source.fromResource("AccessToken.txt").getLines().toList.mkString
  val strava = new Strava(accessTokenString)
  val southWest = BoundCoords(51.466543, -0.009707)
  val northEast = BoundCoords(51.477296, 0.017179)

  val result = for (segments <- strava.segments(southWest, northEast);
                    efforts <- segments.segments.map(segment => strava.leaderboard(segment.id)).toList.sequence)
    yield efforts

  result match {
    case Right(x) => println(x)
    case Left(error) => sys.error(error.toString)
  }
}