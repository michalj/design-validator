package designvalidator.model

import java.io.File
import java.util.zip.ZipFile
import scala.collection.JavaConversions._
import org.objectweb.asm._


object ModelExtractor extends (File => ProjectModel) {
  def apply(file: File) = {
    val zip = new ZipFile(file)
    val classModels = zip.entries().toList.filter(_.getName().endsWith(".class")).map(entry => {
      val reader = new ClassReader(zip.getInputStream(entry))
      val builder = new ClassModelBuilder()
      reader.accept(builder, 0)
      builder()
    })
    LibraryModel(file.getName(), classModels)
  }

  private class ClassModelBuilder extends ClassVisitor {
    def internalizeClassName(s: String) = s.replace('/', '.')
    
    var methods = Seq[MethodModel]()
    var fields = Seq[FieldModel]()
    var name = ""
    var superName = ""
    var interfaces: Seq[String] = _
    var `package` = ""
    def visit(
      version: Int,
      access: Int,
      name: String,
      signature: String,
      superName: String,
      interfaces: Array[String]) {
      this.name = internalizeClassName(name)
      this.superName = superName
      this.interfaces = interfaces.toList
      this.`package` = internalizeClassName(name.substring(0, name.lastIndexOf("/")))
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
      value: Object): FieldVisitor = {
      fields = fields :+ FieldModel(name, signature)
      null
    }
    def visitMethod(
      access: Int,
      name: String,
      desc: String,
      signature: String,
      exceptions: Array[String]): MethodVisitor = {
      new MethodModelBuilder(name, exceptions)
    }
    def visitEnd() {}
    def apply() = ClassModel(name, `package`, methods, fields, superName, interfaces map internalizeClassName)

    private class MethodModelBuilder(name: String, exceptions: Array[String])
      extends MethodVisitor {
      var fieldDependencies = Seq[FieldDependency]()
      var methodDependencies = Seq[MethodDependency]()
      var creates = Seq[String]()
      def visitAnnotationDefault(): AnnotationVisitor = null
      def visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor = null
      def visitParameterAnnotation(
        parameter: Int,
        desc: String,
        visible: Boolean): AnnotationVisitor = null
      def visitAttribute(attr: Attribute) {}
      def visitCode() {}
      def visitFrame(
        `type`: Int,
        nLocal: Int,
        local: Array[Object],
        nStack: Int,
        stack: Array[Object]) {}
      def visitInsn(opcode: Int) {}
      def visitIntInsn(opcode: Int, operand: Int) {}
      def visitVarInsn(opcode: Int, `var`: Int) {}
      def visitTypeInsn(opcode: Int, `type`: String) {
        if (opcode == Opcodes.NEW) {
          creates = creates :+ `type`
        }
      }
      def visitFieldInsn(opcode: Int, owner: String, name: String, desc: String) {
        val static = opcode == Opcodes.GETSTATIC || opcode == Opcodes.PUTSTATIC
        val read = opcode == Opcodes.GETSTATIC || opcode == Opcodes.GETFIELD
        fieldDependencies = fieldDependencies :+ FieldDependency(static, internalizeClassName(owner),
          name, read)
      }
      def visitMethodInsn(opcode: Int, owner: String, name: String, desc: String) {
        methodDependencies = methodDependencies :+ MethodDependency(false,
          internalizeClassName(owner), name)
      }
      def visitJumpInsn(opcode: Int, label: Label) {}
      def visitLabel(label: Label) {}
      def visitLdcInsn(cst: Object) {}
      def visitIincInsn(`var`: Int, increment: Int) {}
      def visitTableSwitchInsn(min: Int, max: Int, dflt: Label, labels: Array[Label]) {}
      def visitLookupSwitchInsn(dflt: Label, keys: Array[Int], labels: Array[Label]) {}
      def visitMultiANewArrayInsn(desc: String, dims: Int) {}
      def visitTryCatchBlock(start: Label, end: Label, handler: Label, `type`: String) {}
      def visitLocalVariable(
        name: String,
        desc: String,
        signature: String,
        start: Label,
        end: Label,
        index: Int) {}
      def visitLineNumber(line: Int, start: Label) {}
      def visitMaxs(maxStack: Int, maxLocals: Int) {}
      def visitEnd() {
        methods = methods :+ MethodModel(name, Seq(), exceptions, null,
          fieldDependencies, methodDependencies, creates)
      }
    }

  }
}
