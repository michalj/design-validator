package designvalidator.graph

import java.io.{Writer, BufferedWriter}

object GraphVizGraphWriter {
  def write(graph: Graph, output: Writer) {
    val out = new BufferedWriter(output)
    out.write("digraph {\n")
    for (verticle <- graph.verticles) {
	  out.write("\t\"" + verticle.id + "\" [label=\"" + verticle.label +
	      "\", fontcolor=" + verticle.color + ",bgcolor=" + verticle.bgcolor +
	      "];\n")      
    }
    for (edge <- graph.edges) {
      out.write("\t\t\"" + edge.fromId + "\" -> \"" + edge.toId + "\";\n")
    }
    out.write("}")
    out.flush()
  }
}