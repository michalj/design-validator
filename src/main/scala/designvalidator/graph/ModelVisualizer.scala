package designvalidator.graph

import designvalidator.model._
import java.io.Writer

object ModelVisualizer {
  def visualize(model: Seq[ClassModel], graphWriter: IGraphWriter) {
    for (classModel <- model) {
      graphWriter.addVerticle(classModel.name, classModel.name)
      for (method <- classModel.methods) {
        val methodId = classModel.name + ":" + method.name
        graphWriter.addVerticle(methodId,
          method.name, bgcolor = "grey")
        graphWriter.addEdge(classModel.name, methodId, "hasMethod")
        for (dependency <- method.methodDependencies) {
          graphWriter.addEdge(methodId,
            dependency.owner + ":" + dependency.name, "calls")
        }
      }
    }
  }
}