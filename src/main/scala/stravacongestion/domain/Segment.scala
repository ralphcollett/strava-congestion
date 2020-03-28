package stravacongestion.domain

import io.circe.{Decoder, HCursor}

case class Segment(id: Int, resourceState: ResourceState, name: String, climbCategory: Int, climbCategoryDesc: String, averageGrade: Double, startLatLong: BoundCoords, endLatLon: BoundCoords, elev_difference: Double, distance: Double, points: String, starred: Boolean)

object Segment {

  implicit val segmentDecoder: Decoder[Segment] = (c: HCursor) => for {
    id <- c.downField("id").as[Int]
    resourceState <- c.downField("resource_state").as[ResourceState]
    name <- c.downField("name").as[String]
    climbCategory <- c.downField("climb_category").as[Int]
    climbCategoryDesc <- c.downField("climb_category_desc").as[String]
    startLatLong <- c.downField("start_latlng").as[Array[Double]]
    endLatLong <- c.downField("end_latlng").as[Array[Double]]
    averageGrade <- c.downField("avg_grade").as[Double]
    elevDifference <- c.downField("elev_difference").as[Double]
    distance <- c.downField("distance").as[Double]
    points <- c.downField("points").as[String]
    starred <- c.downField("starred").as[Boolean]
  } yield Segment(id, resourceState, name, climbCategory, climbCategoryDesc, averageGrade,
    BoundCoords(startLatLong(0), startLatLong(1)), BoundCoords(endLatLong(0), endLatLong(1)), elevDifference,
    distance, points, starred)
}
