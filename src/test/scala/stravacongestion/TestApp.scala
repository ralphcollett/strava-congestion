package stravacongestion

import stravacongestion.domain.BoundCoords

import scala.io.Source

object TestApp extends App {

  val accessTokenString = Source.fromResource("AccessToken.txt").getLines().toList.mkString
  val strava = new Strava(accessTokenString)
  val southWest = BoundCoords(51.466543, -0.009707)
  val northEast = BoundCoords(51.477296, 0.017179)

  strava.segments(southWest, northEast).foreach(println)
}