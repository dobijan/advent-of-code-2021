package jasek.aoc2021

import jasek.aoc2021.utils.Puzzles

import scala.util.Try

object Day1SonarSweep:

  def increases(path: String): Int =
    Puzzles
      .readLines(path)
      .map {
        _
          .map(line => Try(line.trim.toInt).toOption)
          .filter(_.isDefined)
          .flatten
      }
      .map { depths =>
        depths
          .dropRight(1)
          .zip(depths.drop(1))
          .count { case (prev, next) => prev < next }
      }
      .getOrElse(0)
