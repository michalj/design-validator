package designvalidator.model

case class ClassModel(name: String,
  `package`: String,
  methods: Seq[MethodModel],
  fields: Seq[FieldModel],
  superName: String,
  interfaces: Seq[String])

case class MethodModel(name: String, args: Seq[String],
  exceptions: Seq[String],
  returnType: String,
  fieldDependencies: Seq[FieldDependency],
  methodDependencies: Seq[MethodDependency],
  creates: Seq[String])

case class FieldModel(name: String)

case class FieldDependency(static: Boolean, owner: String, name: String,
  read: Boolean)

case class MethodDependency(static: Boolean, owner: String, name: String)