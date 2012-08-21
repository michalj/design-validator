package designvalidator.graph

object SvgTemplate {
  private implicit def int2string(i: Int) = i.toString

  def apply(grid: Int, nodeWidth:Int, params: Parameters, routes: Seq[Seq[Position]]) =
    <svg
		width={ 90 * grid }
  		height={ 90 * grid }
  		xmlns="http://www.w3.org/2000/svg">
      <defs>
        <style type="text/css">
          rect {{
        fill: silver;
        stroke: grey;
        stroke-width: 1;
      }}
      path {{
    	fill: none;
        stroke: black;
        stroke-width: 3;
    	stroke-opacity: 1;
    	stroke-linecap:butt;
    	stroke-linejoin:miter;
    	stroke-opacity:1;
    	marker-start:none;
    	marker-end:url(#Arrow);
      }}
        </style>
        <marker orient="auto" refY="0.0" refX="0.0" id="Arrow" style="overflow:visible;">
          <path d="M 0.0,0.0 L 5.0,-5.0 L -12.5,0.0 L 5.0,5.0 L 0.0,0.0 z " style="fill-rule:evenodd;stroke:#000000;stroke-width:1.0pt;marker-start:none;" transform="scale(0.8) rotate(180) translate(12.5,0)"/>
        </marker>
      </defs>
      <g>
        {
          params.clusterPositions.map({
            case (id, Position(x, y)) => {
              <rect x={ x * grid } y={ y * grid } width={ nodeWidth * grid } height={ (params.nodeOrder(id).size * 3 + 2) * grid }/> ++
                <text x={ (x + 1) * grid } y={ (y + 1) * grid }>
                  { id }
                </text>
            }
          }) ++
            routes.map(route => {
              <path d={
                "M " + route.map({
                  case Position(x, y) => {
                    (x * grid) + "," + (y * grid)
                  }
                }).mkString(" ")
              }/>
            }) ++
            params.clusterPositions.flatMap({
              case (id, Position(x, y)) => {
                params.nodeOrder(id).zipWithIndex.map({
                  case (node, index) => {
                    <rect x={ (x + 1) * grid } y={ (y + index * 3 + 2) * grid } width={ (nodeWidth - 2) * grid } height={ 2 * grid }/> ++
                      <text x={ (x + 2) * grid } y={ (y + index * 3 + 3) * grid }>
                        { node.label }
                      </text>
                  }
                })
              }
            })
        }
      </g>
    </svg>

}