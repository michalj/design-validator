
package sample {
  import designvalidator._
import designvalidator.model._
import java.io.{ OutputStreamWriter, FileOutputStream }
import designvalidator.graph.ModelVisualizer
import designvalidator.graph.GraphVizGraphWriter

  object DAO extends Metaclass(nameMatches(".*DAO"))

  object Helper extends Metaclass(nameMatches(".*Helper"))

  object ServiceInterface extends Metaclass(nameMatches(".*Service"))

  object ServiceMetaclass extends Metaclass(nameMatches(".*ServiceImpl"), extendsOrImplements(ServiceInterface)) {
    mayDependOn(Helper, DAO)
    mayExtend(ServiceInterface)
  }

  class SampleDesign extends Design {
    hasMetaclass(ServiceMetaclass)
  }

  object Test extends App {
    val file = new java.io.File("src/main/resources/commons-logging-1.0.4.jar")
    val model = new ModelExtractor().readJar(file)
    val out = new OutputStreamWriter(new FileOutputStream("graph.dot"))
    GraphVizGraphWriter.write(ModelVisualizer(model), out)
  }
}