package jasek.aoc2021

import org.scalatest.flatspec.AnyFlatSpec

class Day1SonarSweepTest extends AnyFlatSpec :

  it should "count the number of occurrences of increasing depth" in {
    val occurrences = Day1SonarSweep.increases("src/main/resources/day1/input")
    scribe.info(s"Depth has increased $occurrences times.")
  }
