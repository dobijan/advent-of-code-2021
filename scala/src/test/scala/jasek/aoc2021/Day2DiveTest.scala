package jasek.aoc2021

import cats.effect.{IO, IOApp}
import jasek.aoc2021.utils.InputPath

object Day2DiveTest extends IOApp.Simple with InputPath :

  override val inputPath: String = "src/main/resources/day2/input"

  override def run: IO[Unit] =
    for {
      terminalPosition <- Day2Dive.terminalPosition(inputPath)
    } yield scribe.info(s"The terminal position is $terminalPosition. Product: ${terminalPosition.product}")

