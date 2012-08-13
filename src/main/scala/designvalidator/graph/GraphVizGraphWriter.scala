package designvalidator.graph

import java.io.{ Writer, BufferedWriter }

object GraphVizGraphWriter {
  def write(graph: Graph, output: Writer) {
    val out = new BufferedWriter(output)
    out.write("digraph {\n")
    for (cluster <- graph.clusters) {
      out.write("\tsubgraph cluster" + cluster.id + " {\n")
      writeVerticles(out, cluster.verticles)
      out.write("\t}\n")
    }
    writeVerticles(out, graph.verticles)
    for (edge <- graph.edges) {
      out.write("\t\t\"" + edge.fromId + "\" -> \"" + edge.toId + "\";\n")
    }
    out.write("}")
    out.flush()
  }

  def writeVerticles(out: BufferedWriter, verticles: Iterable[Verticle]) {
    for (verticle <- verticles) {
      out.write("\t\"" + verticle.id + "\" [label=\"" + verticle.label +
        "\", fontcolor=" + verticle.color + ",bgcolor=" + verticle.bgcolor +
        "];\n")
    }
  }
}