package jasek.aoc2021

import cats.effect.{IO, IOApp}
import jasek.aoc2021.utils.InputPath

object Day1SonarSweepTest extends IOApp.Simple with InputPath :

  override def inputPath(): String = "src/main/resources/day1/input"

  val run: IO[Unit] =
    for {
      occurrences <- Day1SonarSweep.increases(inputPath())
      windowedOccurrences <- Day1SonarSweep.increasesByWindow(inputPath())
    } yield scribe.info {
      s"""
         |With a 1-wide measurement window depth has increased $occurrences times.
         |With a 3-wide measurement window depth has increased $windowedOccurrences times.
         |""".stripMargin
    }