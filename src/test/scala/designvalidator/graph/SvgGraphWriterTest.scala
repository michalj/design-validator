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
  }
}