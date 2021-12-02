package jasek.aoc2021

import cats.effect.IO
import jasek.aoc2021.utils.Puzzles

import scala.util.Try

object Day1SonarSweep:

  def increases(path: String): IO[Int] =
    for {
      puzzle <- Puzzles.readLines(path)
      depths = puzzle
        .map(line => Try(line.trim.toInt).toOption)
        .filter(_.isDefined)
        .flatten
      count = depths
        .dropRight(1)
        .zip(depths.drop(1))
        .count { case (prev, next) => prev < next }
    } yield count