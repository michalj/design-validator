package designvalidator.graph

case class Graph(name: String, clusters: Seq[Cluster],
  verticles: Set[Verticle], edges: Set[Edge])
case class Cluster(id: String, label: String,
  verticles: Seq[Verticle])
case class Verticle(id: String, label: String,
  color: String = "black", bgcolor: String = "white")
case class Edge(fromId: String, toId: String, label: String)