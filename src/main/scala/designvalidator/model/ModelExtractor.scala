package designvalidator.model

import java.io.File
import java.util.zip.ZipFile
import scala.collection.JavaConversions._
import org.objectweb.asm._

class ModelExtractor {
  def readJar(file: File): Seq[ClassModel] = {
    val zip = new ZipFile(file)
    zip.entries().toList.map(e => { println("r:" + e.getName()); e }).filter(_.getName().endsWith(".class")).map(entry => {
      println("class: " + entry.getName())
      val reader = new ClassReader(zip.getInputStream(entry))
      reader.accept(new ClassModelBuilder(), 0)
    })
    null
  }

  private class ClassModelBuilder extends ClassVisitor {
    def visit(
      version: Int,
      access: Int,
      name: String,
      signature: String,
      superName: String,
      interfaces: Array[String]) {}
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
      println("method: " + name)
      null
    }
    def visitEnd() {}
  }

}
