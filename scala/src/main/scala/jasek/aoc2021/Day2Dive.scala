package jasek.aoc2021

import cats.effect.IO
import jasek.aoc2021.utils.Puzzles

import scala.util.Try

object Day2Dive:

  case class Position(x: Int = 0, z: Int = 0):
    def apply(command: Command): Position = command(this)

    def product: Int = x * z

  sealed trait Command(amount: Int):
    def apply(position: Position): Position

  sealed trait FromName:
    def name: String

    def apply(amount: Int): Command

  object Command:

    case object ForwardCreator extends FromName :
      override val name: String = "forward"

      override def apply(amount: Int): Command = Forward(amount)

    case object DownCreator extends FromName :
      override val name: String = "down"

      override def apply(amount: Int): Command = Deeper(amount)

    case object UpCreator extends FromName :
      override val name: String = "up"

      override def apply(amount: Int): Command = Shallower(amount)

    val creators: Seq[FromName] = Seq(ForwardCreator, DownCreator, UpCreator)

    def fromName(name: String, amount: Int): Option[Command] =
      creators.find(_.name.equals(name)).map(_ (amount))

    def apply(puzzleLine: String): Option[Command] =
      val (literal, amount) = puzzleLine.trim.split("\\s+") match {
        case Array(l, a, _*) => (l, a)
      }
      Try(amount.toInt)
        .toOption
        .flatMap(fromName(literal, _))

  case class Forward(amount: Int) extends Command(amount) :
    override def apply(position: Position): Position =
      position.copy(x = position.x + amount)

  case class Deeper(amount: Int) extends Command(amount) :
    override def apply(position: Position): Position =
      position.copy(z = position.z + amount)

  case class Shallower(amount: Int) extends Command(amount) :
    override def apply(position: Position): Position =
      position.copy(z = position.z - amount)

  def terminalPosition(path: String): IO[Position] =
    for {
      puzzle <- Puzzles.readLines(path)
      position = puzzle
        .flatMap(Command(_))
        .foldLeft(Position())(_ (_))
    } yield position

