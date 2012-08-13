package designvalidator.graph

import designvalidator.model._
import java.io.Writer

object ClassDependenciesVisualizer extends IVisualizer {
  def apply(model: Seq[ClassModel]) = Graph("Dependencies", Seq(),
      model.map(c => Verticle(c.name, c.name, bgcolor = "gray")).toSet,
      model.flatMap(c => {
        c.methods.flatMap(m => {
          m.methodDependencies.map(d => Edge(c.name, d.owner, "<<uses>>"))
        })
      }).toSet)
}