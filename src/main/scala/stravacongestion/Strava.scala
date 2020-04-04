package stravacongestion

import java.net.URL
import java.time.Duration

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
    val url = new URIBuilder("https://www.strava.com/api/v3/segments/explore")
      .addParameter("bounds", s"${southWest.latitude}," +
        s"${southWest.longitude}," +
        s"${northEast.latitude}," +
        s"${northEast.longitude}")
      .addParameter("activity_type", "running")
      .build().toURL
    val response = client.get(url)
    if (response.ok())
      decode[Segments](response.getContent.asString()).left.map(JsonParseError.apply)
    else
      Left(StravaIntegrationError(s"Unexpected reply ${response.getStatusCode} ${response.getStatusMessage}"))
  }
}
