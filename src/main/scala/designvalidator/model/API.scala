package designvalidator.model

case class ClassModel(name: String, constructors: Seq[MethodModel], methods: Seq[MethodModel])
case class MethodModel(name: String, args: Seq[String])