package designvalidator.graph

import math._
import Array._

case class FindRouteProblem(sourcePoints: Seq[Position],
  endPoints: Seq[Position],
  blocked: Array[Array[Int]],
  linksTo: Array[Array[Position]],
  linksFrom: Array[Array[Position]])

object RouteFinder {
  val free = 0
  val blockedHorizontal = 1
  val blockedVertical = 2
  val blocked = 3

  def apply(problem: FindRouteProblem,
    distBuffer: Option[Array[Array[Int]]] = None,
    fromBuffer: Option[Array[Array[Position]]] = None,
    crossPenalty: Int = 5,
    trace: (String, Array[Array[Int]]) => Unit = (t, a) => {}): (Seq[Position], Int) = {
    val FindRouteProblem(sourcePoints, endPoints, blocked,
        linksTo, linksFrom) = problem
    val dist = distBuffer match {
      case Some(b) => b
      case None => ofDim[Int](blocked.length, blocked(0).length)
    }
    val from = fromBuffer match {
      case Some(b) => b
      case None => ofDim[Position](blocked.length, blocked(0).length)
    }
    rangeUpdate(dist, (0, 0, dist.length - 1, dist(0).length - 1),
      Int.MaxValue)
    implicit val positionOrdering = new PositionOrdering(dist, endPoints)
    val queue = new scala.collection.mutable.PriorityQueue[Position]
    def enqueue(pos: Position, d: Int, jumpFrom: Position) {
      dist(pos.x)(pos.y) = d
      from(pos.x)(pos.y) = jumpFrom
      queue += pos
    }
    for (src <- sourcePoints) {
      enqueue(src, 0, src)
    }
    def visit(current: Position) {
      def moveHorizontal(x: Int, y: Int) = blocked(x)(y) match {
        case b if (b == blockedVertical) => crossPenalty
        case b if (b == free) => 1
        case _ => Int.MaxValue
      }
      def moveVertical(x: Int, y: Int) = blocked(x)(y) match {
        case b if (b == blockedHorizontal) => crossPenalty
        case b if (b == free) => 1
        case _ => Int.MaxValue
      }
      val currentDistance = dist(current.x)(current.y)
      val candidates = current match {
        case Position(x, y) => Seq(
          (Position(x + 1, y), moveHorizontal _),
          (Position(x, y + 1), moveVertical _),
          (Position(x - 1, y), moveHorizontal _),
          (Position(x, y - 1), moveVertical _))
      }
      val newPositions = candidates.filter {
        case (Position(x, y), move) => x >= 0 && y >= 0 &&
          x < dist.length && y < dist(0).length &&
          move(x, y) < Int.MaxValue &&
          move(current.x, current.y) < Int.MaxValue &&
          dist(x)(y) > currentDistance + move(x, y) + move(current.x, current.y) &&
          endPoints.map(p => dist(p.x)(p.y)).min > currentDistance + move(x, y)
      }
      newPositions.foreach {
        case (p, move) => {
          enqueue(p, currentDistance + move(p.x, p.y), current)
        }
      }
    }
    trace("Initially", dist)
    while (queue.headOption.isDefined) {
      val current = queue.dequeue
      visit(current)
      trace("After visiting " + current, dist)
    }
    var endPoint = endPoints.minBy(p => dist(p.x)(p.y))
    var current = endPoint
    val cost = dist(current.x)(current.y)
    var route = Seq(current)
    while (dist(current.x)(current.y) > 0) {
      val next = from(current.x)(current.y)
      if (next == null) throw new RuntimeException("No route")
      linksTo(next.x)(next.y) = endPoint
      blocked(next.x)(next.y) += (
        if (next.x == current.x) blockedVertical else blockedHorizontal)
      current = next
      route = Seq(current) ++ route
    }
    val startingPoint = route.head
    for (point <- route) linksFrom(point.x)(point.y) = startingPoint
    (route, cost)
  }

  def rangeUpdate[T](a: Array[Array[T]], range: (Int, Int, Int, Int), value: T) {
    val (x1, y1, x2, y2) = range
    for (i <- x1 to x2) {
      for (j <- y1 to y2) {
        a(i)(j) = value
      }
    }
  }

  def weight(p: Position, dist: Array[Array[Int]], dest: Seq[Position]) = dist(p.x)(p.y) + dest.map {
    d => abs(p.x - d.x) + abs(p.y - d.y)
  }.min

  class PositionOrdering(dist: Array[Array[Int]], dest: Seq[Position]) extends Ordering[Position] {
    def compare(a: Position, b: Position) = {
      weight(b, dist, dest) - weight(a, dist, dest)
    }
  }
}

