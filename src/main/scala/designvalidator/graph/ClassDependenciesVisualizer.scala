package designvalidator.graph

import designvalidator.model._
import java.io.Writer

object ClassDependenciesVisualizer extends IVisualizer {
  def apply(model: ProjectModel) = Graph("Dependencies", Seq(),
      model.classes.map(c => Verticle(c.name, c.name, bgcolor = "gray")).toSet,
      model.classes.flatMap(c => {
        c.methods.flatMap(m => {
          m.methodDependencies.map(d => Edge(c.name, d.owner, "<<uses>>"))
        })
      }).toSet)
}