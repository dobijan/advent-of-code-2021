ThisBuild / scalaVersion := "3.1.0"
ThisBuild / organization := "jasek"

lazy val aoc2021 = (project in file("."))
  .settings(
    name := "advent-of-code-2021",
    libraryDependencies ++= List(
      "org.typelevel" %% "cats-effect" % "3.3.0",
      "com.outr" %% "scribe" % "3.6.3"
    )
  )
