package jasek.aoc2021.utils

import scala.io.{BufferedSource, Source}
import scala.util.{Try, Using}

object Puzzles:
  def readLines(path: String): Try[Vector[String]] =
    readAs(path) { source =>
      source.getLines().toVector
    }

  def read(path: String): Try[String] =
    readAs(path) { source =>
      source.mkString
    }

  private def readAs[RESULT](path: String)(creator: BufferedSource => RESULT): Try[RESULT] =
    Using(Source.fromFile(path, "UTF-8"))(creator)