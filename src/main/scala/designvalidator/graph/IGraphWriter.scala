package designvalidator.graph

trait IGraphWriter {
  def addVerticle(id: String, label: String, color: String = "black",
      bgcolor: String = "white")
  def addEdge(fromId: String, toId: String, label: String)
  
  def begin
  def end
}