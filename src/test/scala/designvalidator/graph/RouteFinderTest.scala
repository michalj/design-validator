package designvalidator.graph

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FeatureSpec
import Array.ofDim

class RouteFinderTest extends FeatureSpec with ShouldMatchers {
  feature("RouteFinder should find shortest route in city-space") {
    scenario("given an obstacle should find way around") {
      // given
      val blocked = Array(
        Array(0, 0, 0, 0),
        Array(0, 1, 1, 0),
        Array(0, 0, 0, 0))
      val startingPoints = Seq(Position(2, 1))
      val endPoints = Seq(Position(0, 1))
      // when
      val path = RouteFinder(FindRouteProblem(startingPoints, endPoints, blocked))
      // then
      path should be (Seq(
          Position(2, 1),
          Position(2, 0),
          Position(1, 0),
          Position(0, 0),
          Position(0, 1)))
    }
  }
}