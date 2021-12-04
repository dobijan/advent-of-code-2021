package jasek.aoc2021

import cats.effect.{IO, IOApp}
import jasek.aoc2021.utils.InputPath

object Day3BinaryDiagnosticTest extends IOApp.Simple with InputPath :

  override val inputPath: String = "src/main/resources/day3/input"

  override def run: IO[Unit] =
    for {
      consumption <- Day3BinaryDiagnostic.consumption(inputPath)
    } yield scribe.info(
      s"""
         |The submarine's consumptions is $consumption
         |""".stripMargin)