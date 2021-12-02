ThisBuild / scalaVersion := "3.1.0"
ThisBuild / organization := "jasek"

lazy val aoc2021 = (project in file("."))
  .settings(
    name := "advent-of-code-2021",
    libraryDependencies ++= List(
      "org.scalatest" %% "scalatest" % "3.2.9" % Test,
      "org.typelevel" %% "cats-core" % "2.7.0",
      "com.outr" %% "scribe" % "3.6.3"
    )
  )
