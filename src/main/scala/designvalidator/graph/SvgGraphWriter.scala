package designvalidator.graph

import java.io.BufferedWriter
import scala.xml.XML
import scala.xml.dtd.DocType
import Array._
import math._

class SvgGraphWriter(grid: Int = 30, crossCost: Int = 10,
  nodeWidth: Int = 10) {

  def write(graph: Graph, out: BufferedWriter) {
    val params = randomParams(graph, nodeWidth)
    val routes = findRoutes(params)
    XML.write(out, SvgTemplate(grid, nodeWidth, params, routes._1), "UTF-8",
      false, null)
    out.close()
  }

  def randomParams(graph: Graph, nodeWidth: Int) = Parameters(
    graph.clusters.zipWithIndex.map {
      case (cluster, index) => {
        (cluster.id -> Position(10 + index * (nodeWidth + 10), 10))
      }
    }.toMap,
    graph.clusters
      .map(cluster => (cluster.id -> cluster.verticles.toSeq.sortBy(_.label)))
      .toMap,
    graph.edges.toSeq.sortBy(e => e.fromId + ":" + e.toId))
  def findRoutes(params: Parameters): (Seq[Seq[Position]], Int) = {
    val routes = toFindRouteProblems(params, nodeWidth).map(p => RouteFinder(p))
    (routes.map(_._1), routes.map(_._2).sum)
  }

  def toFindRouteProblems(params: Parameters, nodeWidth: Int) = {
    val size = params.clusters(nodeWidth).map {
      case (x1, y1, x2, y2) => Position(x2, y2)
    }.reduce((p1, p2) => Position(max(p1.x, p2.x), max(p1.y, p2.y)))
    val blocked = ofDim[Int](size.x + 1, size.y + 1)
    params.clusters(nodeWidth).foreach({
      case (x1, y1, x2, y2) =>
        for (i <- x1 + 1 to x2 - 1; j <- y1 to y2) blocked(i)(j) = RouteFinder.blocked
        for (j <- y1 + 1 to y2 - 1) {
          blocked(x1)(j) = RouteFinder.blockedVertical
          blocked(x2)(j) = RouteFinder.blockedVertical
        }
    })
    params.edgeOrder.map(edge => {
      val endPoints = params.verticlePositions(edge.toId, nodeWidth)
      val sourcePoints = params.verticlePositions(edge.fromId, nodeWidth)
      FindRouteProblem(sourcePoints, endPoints, blocked, null, null)
    })
  }

}

case class Position(x: Int, y: Int)
case class Parameters(
  clusterPositions: Map[String, Position],
  nodeOrder: Map[String, Seq[Verticle]],
  edgeOrder: Seq[Edge]) {
  val verticlePositionsMap = nodeOrder.flatMap {
    case (id, verticles) => verticles.zipWithIndex.map {
      case (v, index) => (v.id, clusterPositions(id) match {
        case Position(x, y) => Position(x, y + 3 + index * 3)
      })
    }
  }
  def clusters(nodeWidth: Int) = new Traversable[(Int, Int, Int, Int)] {
    def foreach[U](visit: ((Int, Int, Int, Int)) => U) {
      clusterPositions
        .map({
          case (id, Position(x, y)) => {
            (x, y, x + nodeWidth, y + nodeOrder(id).size * 3 + 2)
          }
        }).foreach(visit)
    }
  }
  def verticlePositions(id: String, nodeWidth: Int): Seq[Position] = {
    verticlePositionsMap(id) match {
      case Position(x, y) => Seq(Position(x, y), Position(x + nodeWidth, y))
    }
  }
}