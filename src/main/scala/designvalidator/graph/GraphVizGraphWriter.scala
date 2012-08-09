package designvalidator.graph

import java.io.{Writer, BufferedWriter}

class GraphVizGraphWriter(output: Writer) extends IGraphWriter {
  val out = new BufferedWriter(output)
  
  def addVerticle(id: String, label: String, color: String = "black",
      bgcolor: String = "white") { }
  def addEdge(fromId: String, toId: String, label: String) { }
}