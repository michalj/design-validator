package designvalidator.graph

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FeatureSpec
import Array.ofDim

class RouteFinderTest extends FeatureSpec with ShouldMatchers {
  feature("RouteFinder should find shortest route in city-space") {
    scenario("given an obstacle should find way around") {
      // given
      val blocked: Array[Array[Int]] = Seq(
        "...",
        ".X.",
        ".X.",
        "...")
      val startingPoints = Seq(Position(2, 1))
      val endPoints = Seq(Position(0, 1))
      // when
      val path = RouteFinder(FindRouteProblem(startingPoints, endPoints, blocked))
      // then
      path should be(Seq(
        Position(2, 1),
        Position(2, 0),
        Position(1, 0),
        Position(0, 0),
        Position(0, 1)))
    }

    scenario("should decide to cross lines if not blocked in given direction") {
      // given
      val blocked: Array[Array[Int]] = Seq(
        ".|.",
        ".|.")
      val startingPoints = Seq(Position(0, 0))
      val endPoints = Seq(Position(2, 0))
      // when
      val path = RouteFinder(FindRouteProblem(startingPoints, endPoints, blocked))
      // then
      path should be(Seq(
        Position(0, 0),
        Position(1, 0),
        Position(2, 0)))
    }
  }

  implicit def stringarray2intarray(in: Seq[String]): Array[Array[Int]] = {
    val a = ofDim[Int](in(0).length(), in.length)
    for (i <- 0 to a.length - 1; j <- 0 to a(0).length - 1) a(i)(j) = in(j)(i) match {
      case '.' => RouteFinder.free
      case 'X' => RouteFinder.blocked
      case '-' => RouteFinder.blockedHorizontal
      case '|' => RouteFinder.blockedVertical
    }
    a
  }
}