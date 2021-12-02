package jasek.aoc2021

import cats.effect.IO
import jasek.aoc2021.utils.Puzzles

import scala.util.Try

object Day2Dive:

  case class Position(x: Int = 0, z: Int = 0, aim: Int = 0):
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

    case object MoveByAimCreator extends FromName :
      override val name: String = "forward"

      override def apply(amount: Int): Command = MoveByAim(amount)

    case object AimUpCreator extends FromName :
      override val name: String = "up"

      override def apply(amount: Int): Command = AimUp(amount)

    case object AimDownCreator extends FromName :
      override val name: String = "down"

      override def apply(amount: Int): Command = AimDown(amount)

    val creators: Seq[FromName] = Seq(ForwardCreator, DownCreator, UpCreator)

    val aimAwareCreators: Seq[FromName] = Seq(MoveByAimCreator, AimDownCreator, AimUpCreator)

    def fromName(name: String, amount: Int, creators: Seq[FromName]): Option[Command] =
      creators.find(_.name.equals(name)).map(_ (amount))

    def apply(puzzleLine: String, creators: Seq[FromName]): Option[Command] =
      val (literal, amount) = puzzleLine.trim.split("\\s+") match {
        case Array(l, a, _*) => (l, a)
      }
      Try(amount.toInt)
        .toOption
        .flatMap(fromName(literal, _, creators))

  case class Forward(amount: Int) extends Command(amount) :
    override def apply(position: Position): Position =
      position.copy(x = position.x + amount)

  case class Deeper(amount: Int) extends Command(amount) :
    override def apply(position: Position): Position =
      position.copy(z = position.z + amount)

  case class Shallower(amount: Int) extends Command(amount) :
    override def apply(position: Position): Position =
      position.copy(z = position.z - amount)

  case class AimDown(amount: Int) extends Command(amount) :
    override def apply(position: Position): Position =
      position.copy(aim = position.aim + amount)

  case class AimUp(amount: Int) extends Command(amount) :
    override def apply(position: Position): Position =
      position.copy(aim = position.aim - amount)

  case class MoveByAim(amount: Int) extends Command(amount) :
    override def apply(position: Position): Position =
      position.copy(
        x = position.x + amount,
        z = position.z + position.aim * amount
      )

  def terminalPosition(path: String): IO[Position] =
    withCreators(path, Command.creators)

  def terminalPositionByAim(path: String): IO[Position] =
    withCreators(path, Command.aimAwareCreators)

  private def withCreators(path: String, creators: Seq[FromName]) = {
    for {
      puzzle <- Puzzles.readLines(path)
      position = puzzle
        .flatMap(Command(_, creators))
        .foldLeft(Position())(_ (_))
    } yield position
  }
