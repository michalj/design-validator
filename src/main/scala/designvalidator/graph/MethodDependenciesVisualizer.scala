package designvalidator.graph

import designvalidator.model._

class MethodDependenciesVisualizer(filter: ClassModel => Boolean) extends IVisualizer {
  def apply(model: Seq[ClassModel]): Graph = {
    val base = model.filter(filter)
    Graph("Method dependencies",
      base.map(c => Cluster(c.name, c.name,
          c.methods.map(m => Verticle(c.name + "_" + m.name, m.name)).toSet)
      ),
      Set(),
      base.flatMap(c => c.methods.flatMap(m => {
        m.methodDependencies.map(d => Edge(c.name + "_" + m.name, d.owner + "_" + d.name, "<<calls>>"))
      })).toSet
    )
  }
}