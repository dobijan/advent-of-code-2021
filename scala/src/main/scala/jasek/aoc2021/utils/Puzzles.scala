package jasek.aoc2021.utils

import cats.effect.{IO, Resource}

import scala.io.{BufferedSource, Source}

object Puzzles:
  def readLines(path: String): IO[Vector[String]] =
    fileResource(path).use { source =>
      IO(source.getLines().toVector)
    }

  def read(path: String): IO[String] =
    fileResource(path).use { source =>
      IO(source.mkString)
    }

  private def fileResource(path: String): Resource[IO, BufferedSource] =
    Resource.make(IO(Source.fromFile(path, "UTF-8"))) { source =>
      IO(source.close())
    }