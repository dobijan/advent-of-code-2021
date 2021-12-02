package jasek.aoc2021

import cats.effect.IO
import jasek.aoc2021.utils.Puzzles

import scala.util.Try

object Day1SonarSweep:

  def increases(path: String): IO[Int] =
    for {
      depths <- depths(path)
    } yield countIncreases(depths)

  def increasesByWindow(path: String): IO[Int] =
    for {
      depths <- depths(path)
      windowed = depths
        .drop(2)
        .lazyZip(depths.drop(1).dropRight(1))
        .lazyZip(depths.dropRight(2))
        .map { case (prev, current, next) => prev + current + next }
    } yield countIncreases(windowed)

  private def depths(path: String): IO[Vector[Int]] =
    for {
      puzzle <- Puzzles.readLines(path)
    } yield puzzle
      .map(line => Try(line.trim.toInt).toOption)
      .filter(_.isDefined)
      .flatten

  private def countIncreases(depths: Vector[Int]) = {
    depths
      .dropRight(1)
      .zip(depths.drop(1))
      .count { case (prev, next) => prev < next }
  }