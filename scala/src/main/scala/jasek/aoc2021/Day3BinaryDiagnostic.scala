package jasek.aoc2021

import cats.effect.IO
import jasek.aoc2021.Day3BinaryDiagnostic.B
import jasek.aoc2021.utils.Puzzles

import scala.collection.immutable.BitSet

object Day3BinaryDiagnostic:

  type B = (Int, Int)

  type Bits = (B, B, B, B, B, B, B, B, B, B, B, B)

  case class Report(bits: Bits = ((0, 0), (0, 0), (0, 0), (0, 0), (0, 0), (0, 0), (0, 0), (0, 0), (0, 0), (0, 0), (0, 0), (0, 0))) {
    def apply(line: String): Report =
      Report(
        bits = (line.strip(): Seq[Char]) match {
          case Seq(a, b, c, d, e, f, g, h, i, j, k, l) => bits match {
            case (a1, b1, c1, d1, e1, f1, g1, h1, i1, j1, k1, l1) => (
              update(a1, a),
              update(b1, b),
              update(c1, c),
              update(d1, d),
              update(e1, e),
              update(f1, f),
              update(g1, g),
              update(h1, h),
              update(i1, i),
              update(j1, j),
              update(k1, k),
              update(l1, l)
            )
          }
        }
      )

    def gammaRate(): Int =
      val mostCommonBits = mcb()
      (0 to 11).filter(mostCommonBits.contains).map(idxToValue).sum

    def epsilonRate(): Int =
      val mostCommonBits = mcb()
      (0 to 11).filter(!mostCommonBits.contains(_)).map(idxToValue).sum

    private def idxToValue(idx: Int): Int =
      Math.pow(2, 11 - idx).toInt

    private def mcb(): BitSet =
      bits.productIterator
        .zipWithIndex
        .flatMap { case (b, idx) => if isOne(b.asInstanceOf[B]) then Some(idx) else None }
        .foldLeft(BitSet())(_.incl(_))

    private def isOne(pos: B): Boolean =
      pos match {
        case (zeros, ones) => zeros < ones
      }

    private def update(pos: B, c: Char): B = pos match {
      case (zeros, ones) => if c.equals('0') then (zeros + 1, ones) else (zeros, ones + 1)
    }
  }

  def consumption(path: String): IO[Int] =
    for {
      puzzle <- Puzzles.readLines(path)
      report = puzzle.foldLeft(Report())(_ (_))
    } yield report.gammaRate() * report.epsilonRate()