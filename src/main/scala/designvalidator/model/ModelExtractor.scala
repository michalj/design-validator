package designvalidator.model

import java.io.File
import java.util.zip.ZipFile
import scala.collection.JavaConversions._
import org.objectweb.asm._

class ModelExtractor {
  def readJar(file: File): Seq[ClassModel] = {
    val zip = new ZipFile(file)
    zip.entries().toList.map(e => { println("r:" + e.getName()); e }).filter(_.getName().endsWith(".class")).map(entry => {
      val reader = new ClassReader(zip.getInputStream(entry))
      val builder = new ClassModelBuilder()
      reader.accept(builder, 0)
      builder()
    })
  }

  private class ClassModelBuilder extends ClassVisitor {
    var methods = Seq[MethodModel]()
    var name = ""
    def visit(
      version: Int,
      access: Int,
      name: String,
      signature: String,
      superName: String,
      interfaces: Array[String]) {
      this.name = name
    }
    def visitSource(source: String, debug: String) {}
    def visitOuterClass(owner: String, name: String, desc: String) {}
    def visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor = null
    def visitAttribute(attr: Attribute) {}
    def visitInnerClass(
      name: String,
      outerName: String,
      innerName: String,
      access: Int) {}
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
      methods = methods :+ MethodModel(name, Seq(signature))
      null
    }
    def visitEnd() {}
    def apply() = ClassModel(name, Seq(), methods)
  }

}
