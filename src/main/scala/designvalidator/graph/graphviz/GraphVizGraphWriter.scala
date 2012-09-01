package designvalidator.graph.graphviz

import designvalidator.graph._
import java.io._
import scala.sys.process._

object GraphVizGraphWriter extends IGraphWriter {
  def mimeType = "image/png"
  def label = "GraphViz"
  
  def write(graph: Graph, output: OutputStream) {
    val out = new StringWriter()
    out.write("digraph {\n")
    for (cluster <- graph.clusters) {
      out.write("\tsubgraph \"cluster_" + cluster.id + "\" {\n")
      out.write("label = \"" + cluster.label + "\";\n")
      writeVerticles(out, cluster.verticles)
      out.write("\t}\n")
    }
    writeVerticles(out, graph.verticles)
    for (edge <- graph.edges) {
      out.write("\t\t\"" + edge.fromId + "\" -> \"" + edge.toId + "\";\n")
    }
    out.write("}")
    out.close()
    Seq("dot", "-Tpng") #< input(out.toString()) #> output !;
    output.flush()
  }

  private def input(s: String) = new ByteArrayInputStream(s.getBytes("UTF-8"))
  
  private def writeVerticles(out: Writer, verticles: Iterable[Verticle]) {
    for (verticle <- verticles) {
      out.write("\t\"" + verticle.id + "\" [label=\"" + verticle.label +
        "\", fontcolor=" + verticle.color + ",bgcolor=" + verticle.bgcolor +
        "];\n")
    }
  }
}