
package designvalidator.model {
  import java.io.File
  import java.util.zip.ZipFile
  import org.objectweb.asm._
  import scala.collection.JavaConversions._
  
  class ClassModelBuilder extends ClassVisitor {
    def visit(
        version: Int,
        access: Int,
        name: String,
        signature: String,
        superName: String,
        interfaces: Array[String]) { }
    def visitSource(source: String, debug: String) { }
    def visitOuterClass(owner: String, name: String, desc: String) { }
    def visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor = null
    def visitAttribute(attr: Attribute) { }
    def visitInnerClass(
        name: String,
        outerName: String,
        innerName: String,
        access: Int) { }
    def visitField(
        access: Int,
        name: String,
        desc: String,
        signature: String,
        value: Object): FieldVisitor = null
     def visitMethod(
        access: Int,
        name: String,
        desc: String,
        signature: String,
        exceptions: Array[String]): MethodVisitor = {
      println("method: " + name)
      null
    }
     def visitEnd() { }
  }
  
  class ModelExtractor {
    def readJar(file: File): Seq[ClassModel] = {
      val zip = new ZipFile(file)
      zip.entries().toList.map(e => { println("r:" + e.getName()); e}).filter(_.getName().endsWith(".class")).map(entry => {
          println("class: " + entry.getName())
    	  val reader = new ClassReader(zip.getInputStream(entry))
    	  reader.accept(new ClassModelBuilder(), 0)
      })
      null
    }
  }
  case class ClassModel(name: String)
}

package object designvalidator {
  import designvalidator.model._

  class Metaclass(reconize: ClassModel => Boolean*) {
    def mayDependOn(metaclass: Metaclass*) {
      
    }
    def mayExtend(metaclass: Metaclass*) {
      
    }
  }

  class Design {
    def hasMetaclass(metaclass: Metaclass) { }
  }

  def nameMatches(nameRegex: String)(model: ClassModel) = true
  def extendsOrImplements(metaclass: Metaclass)(model: ClassModel) = true
}

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
  
  object Test extends Application {
    val fc = new javax.swing.JFileChooser()
    fc.showOpenDialog(null)
    new ModelExtractor().readJar(fc.getSelectedFile())
  }
}

