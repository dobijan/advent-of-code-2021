package jasek.aoc2021

import cats.effect.{IO, IOApp}

object Day1SonarSweepTest extends IOApp.Simple :

  val run: IO[Unit] =
    for {
      occurrences <- Day1SonarSweep.increases("src/main/resources/day1/input")
    } yield scribe.info(s"Depth has increased $occurrences times.")