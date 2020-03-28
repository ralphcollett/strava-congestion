package stravacongestion.domain

import io.circe.Decoder

sealed trait ResourceState
case object Meta extends ResourceState
case object Summary extends ResourceState
case object Detailed extends ResourceState
case object Unknown extends ResourceState

object ResourceState {

  implicit val resourceStateDecoder: Decoder[ResourceState] = Decoder.decodeInt.map(ResourceState(_))

  def apply(stateId: Int): ResourceState = stateId match {
    case 1 => Meta
    case 2 => Summary
    case 3 => Detailed
    case _ => Unknown
  }
}