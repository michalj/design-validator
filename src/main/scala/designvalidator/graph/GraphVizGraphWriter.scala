package designvalidator.graph

import java.io.{Writer, BufferedWriter}

class GraphVizGraphWriter(output: Writer) extends IGraphWriter {
  val out = new BufferedWriter(output)
  
  def begin {
    out.write("digraph {")
  }
  
  def addVerticle(id: String, label: String, color: String = "black",
      bgcolor: String = "white") {
	  out.write(id + "[label=\"" + label + "\", fontcolor=" + color + ",color=" + bgcolor + "];")
  }
  def addEdge(fromId: String, toId: String, label: String) {
    out.write(fromId + "->" + toId + ";")
  }
  
  def end {
    out.write("}")
  }
}