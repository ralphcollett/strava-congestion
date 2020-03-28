package stravacongestion

import java.net.URL
import java.time.Duration

import io.circe.parser._
import org.apache.http.client.utils.URIBuilder
import simplehttp.HttpClients.anApacheClient
import simplehttp.configuration.AccessToken.accessToken
import simplehttp.configuration.HttpTimeout.httpTimeout
import simplehttp.configuration.OAuthCredentials.oAuth
import stravacongestion.domain.{BoundCoords, Segments}
import scala.io.Source

object TestApp extends App {

  val accessTokenString = Source.fromResource("AccessToken.txt").getLines().toList.mkString
  val credentials = oAuth(accessToken(accessTokenString), new URL("https://www.strava.com/api"))
  val southWest = BoundCoords(51.466543, -0.009707)
  val northEast = BoundCoords(51.477296, 0.017179)

  val client = anApacheClient().`with`(credentials).`with`(httpTimeout(Duration.ofMinutes(1)))
  val url = new URIBuilder("https://www.strava.com/api/v3/segments/explore")
    .addParameter("bounds", s"${southWest.latitude}," +
      s"${southWest.longitude}," +
      s"${northEast.latitude}," +
      s"${northEast.longitude}")
    .addParameter("activity_type", "running")
    .build().toURL

  val response = client.get(url)
  if (response.ok())
    decode[Segments](response.getContent.asString()).left.map(error => error.printStackTrace()).foreach(println)
  else
    println(s"Unexpected reply ${response.getStatusCode} ${response.getStatusMessage}")

}