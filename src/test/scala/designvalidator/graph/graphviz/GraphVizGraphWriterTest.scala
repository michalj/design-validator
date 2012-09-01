package designvalidator.graph.graphviz

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FeatureSpec
import designvalidator.graph._
import java.io.OutputStream

class GraphVizGraphWriterTest extends FeatureSpec with ShouldMatchers
  with PendingHelper {

  val writer = GraphVizGraphWriter

  feature("GraphVizGraphWriter can generate graph vizualizations") {
    scenario("can generate png") {
      pendingUnlessDotAvailable {
        // given
        val graph = Graph("sample", Seq(), Set(
          Verticle("a", "A"), Verticle("b", "B")), Set(
          Edge("a", "b", "AtoB")))
        val output = new OutputStreamSize
        // when
        writer.write(graph, output)
        // then
        output.bytesWritten should be > (10)
      }
    }
  }
}

class OutputStreamSize extends OutputStream {
  private var count = 0
  def write(b: Int) { count += 1 }
  def bytesWritten = count
}