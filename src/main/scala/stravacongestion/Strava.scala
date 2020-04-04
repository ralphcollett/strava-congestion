package stravacongestion

import java.net.URL
import java.time.Duration

import cats.implicits._
import io.circe.Decoder
import io.circe.parser.decode
import org.apache.http.client.utils.URIBuilder
import simplehttp.HttpClients.anApacheClient
import simplehttp.configuration.AccessToken.accessToken
import simplehttp.configuration.HttpTimeout.httpTimeout
import simplehttp.configuration.OAuthCredentials.oAuth
import stravacongestion.domain._

class Strava(accessTokenString: String) {

  private val credentials = oAuth(accessToken(accessTokenString), new URL("https://www.strava.com/api"))
  private val client = anApacheClient().`with`(credentials).`with`(httpTimeout(Duration.ofMinutes(1)))

  def segments(southWest: BoundCoords, northEast: BoundCoords): Either[StravaError, Segments] = {
    get[Segments](buildUri(exploreSegmentsResource,
      List(("bounds", s"${southWest.latitude},${southWest.longitude},${northEast.latitude},${northEast.longitude}"),
        ("activity_type", "running")
      )
    ))
  }

  def leaderboard(segmentId: SegmentId): Either[StravaError, Leaderboard] = {
    Range(1, 20).toList.map(
      pageNumber => get[Leaderboard](buildUri(leaderBoardResource(segmentId),
        List(("date_range", "this_week"), ("page", pageNumber.toString), ("per_page", "200"))))
    ).sequence.map(_.reduce(_.merge(_)))
  }

  private def get[A: Decoder](url: URL): Either[StravaError, A] = {
    val response = client.get(url)
    if (response.ok())
      decode[A](response.getContent.asString()).left.map(JsonParseError.apply)
    else
      Left(StravaIntegrationError(s"Unexpected reply ${response.getStatusCode} ${response.getStatusMessage}"))
  }

  private val baseUri = "https://www.strava.com/api/v3/"
  private val exploreSegmentsResource = baseUri + "segments/explore"
  private def leaderBoardResource(segmentId: SegmentId) = baseUri + s"segments/${segmentId.value}/leaderboard"

  private def buildUri(uri: String, params: List[(String, String)]) = {
    val baseUri = new URIBuilder(uri)
    params.foldLeft(baseUri)((uri, param) => uri.addParameter(param._1, param._2)).build().toURL
  }
}
