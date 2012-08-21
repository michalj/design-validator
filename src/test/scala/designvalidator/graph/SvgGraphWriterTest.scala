package designvalidator.graph

import org.scalatest.FeatureSpec
import org.scalatest.matchers.ShouldMatchers
import java.io._

class SvgGraphWriterTest extends FeatureSpec with ShouldMatchers {
  feature("SvgGraphWriter generates SVG layout") {
    scenario("1") {
      val writer = new SvgGraphWriter
      val out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("target/graph.svg")))
      writer.write(Graph("Class Diagram",
        Seq(
          Cluster("Service", "Service", Set(
            Verticle("Service.do", "do"),
            Verticle("Service.serve", "serve"))),
          Cluster("Util", "Util", Set(
            Verticle("Util.help", "help")))),
        Set(),
        Set(Edge("Service.do", "Util.help", "calls"))), out)
    }

    scenario("Can translate layout Params into FindRouteProblem list") {
      // given
      val params = sampleParams
      val writer = new SvgGraphWriter
      // when
      val problems = writer.toFindRouteProblems(params, 10)
      // then
      val FindRouteProblem(sourcePoints, endPoints, blocked) = problems(0)
      sourcePoints should be (Seq(Position(13, 4), Position(23, 4)))
      endPoints should be (Seq(Position(1, 4), Position(11, 4)))
      sourcePoints foreach { s => blocked(s.x)(s.y) should be (0) }
      endPoints foreach { s => blocked(s.x)(s.y) should be (0) }
    }

    def sampleParams = Parameters(
      Map(
        "Util" -> Position(1, 1),
        "Service" -> Position(13, 1)),
      Map(
        "Service" -> Seq(
          Verticle("Service.do", "do"),
          Verticle("Service.serve", "serve")),
        "Util" -> Seq(
          Verticle("Util.help", "help"))),
      Seq(Edge("Service.do", "Util.help", "calls")))
  }
}