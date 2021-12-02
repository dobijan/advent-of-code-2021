package jasek.aoc2021

import cats.effect.{IO, IOApp}
import jasek.aoc2021.utils.InputPath

object Day2DiveTest extends IOApp.Simple with InputPath :

  override val inputPath: String = "src/main/resources/day2/input"

  override def run: IO[Unit] =
    for {
      terminalPosition <- Day2Dive.terminalPosition(inputPath)
      terminalPositionByAim <- Day2Dive.terminalPositionByAim(inputPath)
    } yield scribe.info(
      s"""
         |The terminal position by cartesian movement is $terminalPosition. Product: ${terminalPosition.product}
         |The terminal position by polar movement is $terminalPositionByAim. Product: ${terminalPositionByAim.product}
         |""".stripMargin)

