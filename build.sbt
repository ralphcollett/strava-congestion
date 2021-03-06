name := "stravacongestion"

version := "0.1"

scalaVersion := "2.12.4"

scalacOptions += "-Ypartial-unification"

val simpleHttpVersion = "1.4"
val circeVersion = "0.13.0"
val catsVersion = "2.0.0"

libraryDependencies ++= Seq(
  "com.simple-http" % "simple-http" % simpleHttpVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.typelevel" %% "cats-core" % catsVersion
)
