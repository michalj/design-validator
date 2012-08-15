package designvalidator.graph

import java.io.BufferedWriter
import scala.xml.XML
import scala.xml.dtd.DocType

class SvgGraphWriter(grid: Int = 30, crossCost: Int = 10,
  nodeWidth: Int = 10) {

  implicit def int2string(i: Int) = i.toString
  
  def write(graph: Graph, out: BufferedWriter) {
    val params = randomParams(graph)
    val routes = findRoutes(params)
    val svg = <svg width={(25 * grid).toString} height={(10 * grid).toString} xmlns="http://www.w3.org/2000/svg">
<defs>
    <style type="text/css"><![CDATA[
      rect {
        fill: silver;
        stroke: grey;
        stroke-width: 1;
      }
      path {
    	fill: none;
        stroke: black;
        stroke-width: 3;
    	stroke-opacity: 1;
    	stroke-linecap:butt;
    	stroke-linejoin:miter;
    	stroke-opacity:1;
    	marker-start:none;
    	marker-end:url(#Arrow);
      }
    ]]></style>
    <marker
       orient="auto"
       refY="0.0"
       refX="0.0"
       id="Arrow"
       style="overflow:visible;">
      <path
         d="M 0.0,0.0 L 5.0,-5.0 L -12.5,0.0 L 5.0,5.0 L 0.0,0.0 z "
         style="fill-rule:evenodd;stroke:#000000;stroke-width:1.0pt;marker-start:none;"
         transform="scale(0.8) rotate(180) translate(12.5,0)" />
    </marker>
  </defs>
    	<g>
    		{
    			params.clusterPositions.map({case (id, Position(x, y)) => {
    			  <rect
    				x={x * grid}
    				y={y * grid}
    			  	width={nodeWidth * grid}
    				height={(params.nodeOrder(id).size * 3 + 2) * grid} /> ++
    			  <text x={(x + 1) * grid} y={(y + 1) * grid}>
    			  	{id}
    			  </text>
    			}}) ++
    			routes.map(route => {
    			  <path d={"M " + route.map({case Position(x, y) => {
    			    (x * grid) + "," + (y * grid)
    			  }}).mkString(" ")} />
    			}) ++
    			params.clusterPositions.flatMap({case (id, Position(x, y)) => {
    			  params.nodeOrder(id).zipWithIndex.map({case (node, index) => {
    			    <rect x={(x + 1) * grid} y={(y + index * 3 + 2) * grid}
    			    	width={(nodeWidth - 2) * grid} height={2 * grid} /> ++
    			    <text x={(x + 2) * grid} y={(y + index * 3 + 3) * grid}>
    			    	{node.label}
    			    </text>
    			  }})
    			}})
    		}
    	</g>
    </svg>
    XML.write(out, svg, "UTF-8", false, null)
    out.close()
  }

  def randomParams(graph: Graph) = Parameters(
    Map(
      "Util" -> Position(1, 1),
      "Service" -> Position(13, 1)),
    Map(
      "Service" -> Seq(
        Verticle("Service.do", "do"),
        Verticle("Service.serve", "serve")),
      "Util" -> Seq(
        Verticle("Util.help", "help"))),
    Seq(Edge("Service.do", "Util.help", "calls")))
  def findRoutes(params: Parameters): Seq[Seq[Position]] = Seq(
    Seq(Position(14, 4), Position(10, 4)),
    Seq(Position(22, 4), Position(24, 4), Position(24, 7), Position(22, 7)))
}

case class Position(x: Int, y: Int)
case class Parameters(
  clusterPositions: Map[String, Position],
  nodeOrder: Map[String, Seq[Verticle]],
  edgeOrder: Seq[Edge])