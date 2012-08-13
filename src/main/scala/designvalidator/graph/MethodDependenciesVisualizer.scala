package designvalidator.graph

import designvalidator.model._

class MethodDependenciesVisualizer(filter: ClassModel => Boolean) extends IVisualizer {
  def apply(model: Seq[ClassModel]): Graph = {
    val base = model.filter(filter)
    val classes = base.map(c => c.name).toSet
    Graph("Method dependencies",
      base.map(c => Cluster(c.name, c.name,
          c.methods.map(m => Verticle(c.name + "_" + m.name, m.name)).toSet)
      ),
      Set(),
      base.flatMap(c => c.methods.flatMap(m => {
        m.methodDependencies
          .filter(d => classes.contains(d.owner))
          .map(d => Edge(c.name + "_" + m.name, d.owner + "_" + d.name, "<<calls>>"))
      })).toSet ++
      base.map(c => Edge("cluster_" + c.name, "cluster_" + c.superName, "<<extends>>")) ++
      base.flatMap(c => c.interfaces.map(i => Edge("cluster_" + c.name, "cluster_" + i, "<<implements>>")))
    )
  }
}