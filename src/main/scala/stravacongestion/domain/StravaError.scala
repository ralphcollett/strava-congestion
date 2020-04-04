package stravacongestion.domain

sealed trait StravaError
case class StravaIntegrationError(cause: String) extends StravaError
case class JsonParseError(cause: Throwable) extends StravaError
