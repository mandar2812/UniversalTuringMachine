package in.tamchow.turing

import scala.language.postfixOps

/**
  * Encapsulates a single possible operation of a Turing Machine, but here I call it the "Command"
  */
object TuringCommand {
  val (matchAnythingCode, nullCode, commandArity, whitespaceRegex) = ("*", "!", 5, "\\s+")

  /**
    * Allowed wildcards:
    * <br>
    * 1. '!' - indicates a null value or the halting command, internally represented as `null`
    * 2. '*' - indicates a match-all wildcard character, no separate internal representation
    * <br>
    * Separator used is whitespace - 5 terms are required after tokenization
    *
    * @param data [[String]] indicating a defined transitional operation
    * @return a [[TuringCommand]] object derived from the argument
    * @see [[matchAnythingCode]]
    * @see [[nullCode]]
    * @see [[commandArity]]
    * @see [[whitespaceRegex]]
    */
  def apply(data: String): TuringCommand = {
    val elements = escapeNull((data split(whitespaceRegex, commandArity)).toVector)
    TuringCommand((elements.head, elements(1), elements(2), elements(3), MoveDirection parse elements.last))
  }

  def escapeNull(elements: Vector[String]) = elements map {
    case `nullCode` => null
    case others => others
  }

  def apply(data: (String, String, String, String, MoveDirection)): TuringCommand =
    new TuringCommand(data._1, data._2, data._3, data._4, data._5)
}

case class TuringCommand private(_currentState: String, _nextState: String, _currentValue: String, _nextValue: String, _direction: MoveDirection) {

  import TuringCommand._

  def valueMatchesEverything = currentValue == matchAnythingCode

  def currentValue = _currentValue

  def stateMatchesEveryThing = currentState == matchAnythingCode

  /**
    * The output format is deliberately similar to the input format,
    * so the result of one can be reflexively used with the other.
    *
    * @return a [[String]] representing this [[TuringCommand]] object
    */
  override def toString = Vector(currentState, nextState, currentValue, nextValue, direction.name) map {
    case nullItem if nullItem == null => nullCode
    case notNullItem => notNullItem
  } mkString " "

  def currentState = _currentState

  def nextState = _nextState

  def nextValue = _nextValue

  def direction = _direction
}