package designvalidator.graph

import java.io.{Writer, BufferedWriter}

class GraphVizGraphWriter(output: Writer) extends IGraphWriter {
  val out = new BufferedWriter(output)
  
  def begin {
    out.write("digraph {\n")
  }
  
  def addVerticle(id: String, label: String, color: String = "black",
      bgcolor: String = "white") {
	  out.write("\t\"" + id + "\" [label=\"" + label + "\", fontcolor=" + color + ",color=" + bgcolor + "];\n")
  }
  def addEdge(fromId: String, toId: String, label: String) {
    out.write("\t\t\"" + fromId + "\"->\"" + toId + "\";\n")
  }
  
  def end {
    out.write("}")
    out.flush
    out.close
  }
}