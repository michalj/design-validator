package designvalidator.graph

import math._
import Array._

case class FindRouteProblem(sourcePoints: Seq[Position],
    endPoints: Seq[Position],
    blocked: Array[Array[Int]])

object RouteFinder {
  val free = 0
  val blockedHorizontal = 1
  val blockedVertical = 2
  val blocked = 3
  
  def apply(problem: FindRouteProblem, buffer: Option[Array[Array[Int]]] = None,
      crossPenalty: Int = 5): Seq[Position] = {
    val FindRouteProblem(sourcePoints, endPoints, blocked) = problem
    val dist = buffer match {
      case Some(b) => b
      case None => ofDim[Int](blocked.length, blocked(0).length)
    }
    rangeUpdate(dist, (0, 0, dist.length - 1, dist(0).length - 1),
      Int.MaxValue)
    implicit val positionOrdering = new PositionOrdering(dist, endPoints)
    val queue = new scala.collection.mutable.PriorityQueue[Position]
    def enqueue(pos: Position, d: Int) {
      dist(pos.x)(pos.y) = d
      queue += pos
    }
    for (src <- sourcePoints) {
      enqueue(src, 0)
    }
    def visit(current: Position) {
      val newDistance = dist(current.x)(current.y) + 1
      val candidates = current match {
        case Position(x, y) => Seq(
          Position(x + 1, y),
          Position(x, y + 1),
          Position(x - 1, y),
          Position(x, y - 1))
      }
      val newPositions = candidates.filter {
        case Position(x, y) => x >= 0 && y >= 0 &&
          x < dist.length && y < dist(0).length &&
          dist(x)(y) > newDistance &&
          blocked(x)(y) == 0 &&
          endPoints.map(p => dist(p.x)(p.y)).min > newDistance
      }
      newPositions.foreach(p => enqueue(p, newDistance))
    }
    while (queue.headOption.isDefined) {
      visit(queue.dequeue)
    }
    var current = endPoints.minBy(p => dist(p.x)(p.y))
    var route = Seq(current)
    while (dist(current.x)(current.y) > 0) {
      current = Seq(
        Position(current.x + 1, current.y),
        Position(current.x, current.y + 1),
        Position(current.x - 1, current.y),
        Position(current.x, current.y - 1))
        .filter(p => p.x >= 0 && p.y >= 0 &&
          p.x < dist.length && p.y < dist(0).length &&
          dist(p.x)(p.y) == dist(current.x)(current.y) - 1)
        .head
      route = Seq(current) ++ route
    }
    route
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

