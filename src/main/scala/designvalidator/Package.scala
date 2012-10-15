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