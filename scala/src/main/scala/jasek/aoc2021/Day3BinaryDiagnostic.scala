package jasek.aoc2021

import cats.effect.IO
import jasek.aoc2021.utils.Puzzles

import scala.annotation.tailrec
import scala.collection.immutable.BitSet

object Day3BinaryDiagnostic:

  object Report:
    def apply(path: String): IO[Report] =
      for {
        puzzle <- Puzzles.readLines(path)
        bitCount = puzzle(0).trim.length
        report = puzzle.foldLeft(Report(bitCount))(_ (_))
      } yield report

  case class Report(bitCount: Int, numbers: Seq[Long] = Seq()):
    def apply(line: String): Report =
      this.copy(numbers = numbers :+ line.strip().dropWhile(_.equals('0')).toLong)

    // Task 1
    def powerConsumption(): Int =
      val mcb = BitStats(this.numbers).toBitSet
      gammaRate(mcb) * epsilonRate(mcb)

    private def gammaRate(mcb: BitSet): Int =
      (0 until bitCount).filter(mcb.contains).map(idx => idxToValue(bitCount - 1 - idx)).sum

    private def epsilonRate(mcb: BitSet): Int =
      (0 until bitCount).filter(!mcb.contains(_)).map(idx => idxToValue(bitCount - 1 - idx)).sum

    private def idxToValue(idx: Int): Int =
      Math.pow(2, idx).toInt

    // Task 2
    def lifeSupportRating(): Long =
      oxygenGeneratorRating * co2ScrubberRating

    private def oxygenGeneratorRating: Long =
      toBinary(
        findNumber(criterion = (depth: Int, mostCommonBits: BitStats) => (number: Long) =>
          criterion(
            depth,
            mostCommonBits,
            number,
            (depthInBitSet: Boolean, digit: Long) => depthInBitSet && digit != 0 || !depthInBitSet && digit == 0
          )
        )
      )

    private def co2ScrubberRating: Long =
      toBinary(
        findNumber(criterion = (depth: Int, mostCommonBits: BitStats) => (number: Long) =>
          criterion(
            depth,
            mostCommonBits,
            number,
            (depthInBitSet: Boolean, digit: Long) => depthInBitSet && digit == 0 || !depthInBitSet && digit != 0
          )
        )
      )

    private def criterion(
      depth: Int,
      mostCommonBits: BitStats,
      number: Long,
      comparator: (depthInBitSet: Boolean, digit: Long) => Boolean
    ): Boolean = comparator(
      depthInBitSet = mostCommonBits.toBitSet.contains(depth),
      digit = (number / Math.pow(10, bitCount - 1 - depth).toLong) % 10
    )

    /**
     * Recursively filters the list of numbers until one remains.
     * It takes:
     *  - the numbers to filter
     *  - the current depth which signals which digit to examine
     *  - and the criterion to filter by.
     *
     * The criterion takes a depth and a bit stats and returns a lambda that can filter the numbers for that depth and bits stats
     */
    @tailrec
    private def findNumber(
      numbers: Seq[Long] = this.numbers,
      depth: Int = 0,
      criterion: (Int, BitStats) => Long => Boolean
    ): Long =
      if numbers.size < 2
      then numbers.head
      else findNumber(
        numbers = numbers.filter(criterion(depth, BitStats(numbers))),
        depth = depth + 1,
        criterion = criterion
      )

    private def toBinary(decimal: Long): Long =
      (0 until bitCount)
        .flatMap(idx => if ((decimal / Math.pow(10, idx).toLong) % 10) != 0 then Some(idxToValue(idx)) else None)
        .sum

    case class BitStats(bitCount: Int, bits: Vector[(Int, Int)] = Vector.fill(bitCount)((0, 0))):
      // set that contains indices at which '1' is the more common bit
      def toBitSet: BitSet =
        bits
          .zipWithIndex
          .filter { case ((zeros, ones), _) => zeros <= ones }
          .map { case (_, idx) => idx }
          .foldLeft(BitSet())(_.incl(_))

    object BitStats:
      /**
       * Calculates the set of indices at which '1' is the more common bit, for a set of numbers.
       */
      def apply(numbers: Seq[Long]): BitStats =
        numbers
          .map(n => (0 until bitCount).map(idx => (n / Math.pow(10, bitCount - 1 - idx).toLong) % 10))
          .foldLeft(BitStats(bitCount)) { (acc, digits) =>
            digits
              .zipWithIndex
              .foldLeft(acc) { case (bitStats, (digit, idx)) =>
                val statsAtIdx@(zeros, ones) = bitStats.bits(idx)
                if digit == 0
                then bitStats.copy(bits = bitStats.bits.updated(idx, statsAtIdx.copy(_1 = zeros + 1)))
                else bitStats.copy(bits = bitStats.bits.updated(idx, statsAtIdx.copy(_2 = ones + 1)))
              }
          }