package designvalidator.graph

import designvalidator.model._
import designvalidator.graph.svg.SvgTemplate

class MethodDependenciesVisualizer(filter: ClassModel => Boolean) extends IVisualizer {
  def apply(model: ProjectModel): Graph = {
    val base = model.classes.filter(filter)
    val classes = base.map(c => c.name).toSet
    val allVerticles = base.flatMap(c => {
      Seq(c.name) ++
        c.methods.map(m => c.name + "_" + m.name)
    }).toSet
    val allEdges = base.flatMap(c => c.methods.flatMap(m => {
      m.methodDependencies
        .filter(d => classes.contains(d.owner))
        .map(d => Edge(c.name + "_" + m.name, d.owner + "_" + d.name, "<<calls>>"))
    })).toSet
    /*
    ++
      base.map(c => Edge(c.name, c.superName, "<<extends>>")) ++
      base.flatMap(c => c.interfaces.map(i => Edge(c.name, i, "<<implements>>")))
    */
    Graph("Method dependencies",
      base.map(c => Cluster(c.name, c.name,
        c.methods.map(m => Verticle(c.name + "_" + m.name, m.name)).toSet)),
      Set(),
      allEdges.filter(e => allVerticles.contains(e.toId)))
  }
}