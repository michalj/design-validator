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
      val linksTo = ofDim[Position](3, 4)
      val linksFrom = ofDim[Position](3, 4)
      // when
      val path = RouteFinder(FindRouteProblem(startingPoints, endPoints, blocked,
        linksTo, linksFrom))
      // then
      path should be((Seq(
        Position(2, 1),
        Position(2, 0),
        Position(1, 0),
        Position(0, 0),
        Position(0, 1)), 4))
      linksTo(2)(1) should be(endPoints.head)
      linksTo(0)(0) should be(endPoints.head)
      linksFrom(0)(1) should be(startingPoints.head)
      linksFrom(1)(0) should be(startingPoints.head)
    }

    scenario("should decide to cross lines if not blocked in given direction") {
      // given
      val blocked: Array[Array[Int]] = Seq(
        ".|.",
        ".|.",
        ".|.",
        ".|.",
        ".|.",
        "...")
      val startingPoints = Seq(Position(0, 0))
      val endPoints = Seq(Position(2, 0))
      val linksTo = ofDim[Position](3, 4)
      val linksFrom = ofDim[Position](3, 4)
      // when
      val path = RouteFinder(FindRouteProblem(startingPoints, endPoints, blocked,
        linksTo, linksFrom),
        crossPenalty = 5)
      // then
      path should be((Seq(
        Position(0, 0),
        Position(1, 0),
        Position(2, 0)), 6))
    }
  }

  scenario("should join routes going to the same destination") { pendingUntilFixed {
    // given
    val blocked: Array[Array[Int]] = Seq(
      "---",
      ".X.")
    val linksTo = ofDim[Position](3, 2)
    linksTo(1)(0) = Position(0, 0)
    linksTo(2)(0) = Position(0, 0)
    val linksFrom = ofDim[Position](3, 2)
    val startingPoints = Seq(Position(2, 1))
    val endPoints = Seq(Position(0, 0))
    // when
    val path = RouteFinder(FindRouteProblem(startingPoints, endPoints, blocked,
        linksTo, linksFrom), trace = debugTrace)
    // then
    path should be ((Seq(
        Position(2, 1),
        Position(2, 0),
        Position(1, 0),
        Position(0, 0)), 1))
  }}

  scenario("should branch routes starting at the same source") {
    pending
  }

  def debugTrace(label: String, a: Array[Array[Int]]) {
    println(label)
    a.foreach(l => println(l.toList))
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