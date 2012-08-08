
package sample {
  import designvalidator._
  import designvalidator.model._

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
    val fc = new javax.swing.JFileChooser()
    fc.showOpenDialog(null)
    val model = new ModelExtractor().readJar(fc.getSelectedFile())
    println("model: " + model)
  }
}