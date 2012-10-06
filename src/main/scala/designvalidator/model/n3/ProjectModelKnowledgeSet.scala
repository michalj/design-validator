package designvalidator.model.n3

import designvalidator.model._
import eu.semantiq.semblade._
import designvalidator.model.n3.DesignImplicits._

object ProjectModelKnowledgeSet extends (ProjectModel => KnowledgeSet) {

  def apply(model: ProjectModel) = model match {
    case model: AppModel => KnowledgeSet("app:" + model.name, describeApp(model),
      Seq(),
      Seq())
    case model: LibraryModel => KnowledgeSet("lib:" + model.name,
      describeLibrary(model),
      Seq(),
      Seq())
  }

  private def describeApp(model: AppModel) = {
    val appDescription = model.libraries map (l => Triple(
      UriNode("lib:" + l.name),
      UriNode("d:belongsToApp"),
      UriNode("app:" + model.name),
      true))
    appDescription ++ (model.libraries flatMap describeLibrary)
  }

  private def classUri(name: String): UriNode = UriNode("class:" + name)
  private def classUri(c: ClassModel): UriNode = classUri(c.name)
  private def methodUri(c: ClassModel, m: MethodModel): UriNode =
    methodUri(c.name, m.name + "(" + m.args.mkString(",") + ")")
  private def methodUri(className: String, methodSignature: String): UriNode =
    UriNode("method:" + className + "/" + methodSignature)
  private def interfaceUri(s: String) = UriNode(s)
  private def fieldUri(c: ClassModel, f: FieldModel): UriNode = fieldUri(c.name, f.name)
  private def fieldUri(className: String, fieldName: String) =
    UriNode("field:" + className + "/" + fieldName)

  private def describeLibrary(l: LibraryModel) = {
    def describeClass(c: ClassModel) = {
      def describeInterface(name: String) = Seq(
        Triple(classUri(c), "d:implements", interfaceUri(name)),
        Triple(interfaceUri(name), "rdf:type", "d:Interface"))

      def describeMethod(m: MethodModel) = {
        def fieldAccessVerb(fd: FieldDependency) = if (fd.read) "d:reads" else "d:writes"
        (m.creates map (cc => Triple(methodUri(c, m), "d:creates", classUri(cc)))) ++
          (m.fieldDependencies map (fd => Triple(methodUri(c, m), fieldAccessVerb(fd), fieldUri(fd.owner, fd.name)))) ++
          (m.methodDependencies map (md => Triple(methodUri(c, m), "d:invokes", methodUri(md.owner, md.name))))
          Seq(Triple(classUri(c), "d:hasMethod", methodUri(c, m)))
      }

      def describeField(f: FieldModel) = Seq(
        Triple(classUri(c), "d:hasField", fieldUri(c, f)),
        Triple(fieldUri(c, f), "d:hasType", UriNode("fieldtype:" + f.`type`)))

      (c.interfaces flatMap describeInterface) ++
        (c.methods flatMap describeMethod) ++
        (c.fields flatMap describeField) ++
        Seq(Triple(classUri(c), "d:hasQualifiedName", ValueNode(c.name, "xsd:string")),
          Triple(classUri(c), "d:belongsToLibrary", UriNode("lib:" + l.name), true))
    }
    l.classes flatMap describeClass
  }
}