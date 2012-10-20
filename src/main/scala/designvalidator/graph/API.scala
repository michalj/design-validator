package designvalidator.graph

import java.io.OutputStream
import designvalidator.model.ProjectModel

case class Graph(name: String, clusters: Seq[Cluster],
  verticles: Set[Verticle], edges: Set[Edge])
case class Cluster(id: String, label: String,
  verticles: Set[Verticle])
case class Verticle(id: String, label: String,
  color: String = "black", bgcolor: String = "white")
case class Edge(fromId: String, toId: String, label: String)

trait IVisualizer extends (ProjectModel => Graph)

trait IGraphWriter {
  def write(graph: Graph, out: OutputStream)
  def label: String
  def mimeType: String
}