package designvalidator.model.n3

import org.scalatest.FeatureSpec
import org.scalatest.matchers.ShouldMatchers
import designvalidator.model._
import eu.semantiq.semblade._

class ProjectModelKnowledgeSetTest extends FeatureSpec with ShouldMatchers {

  feature("ProjectModel can act as a KnowledgeSource") {
    scenario("Class belongs to a Library") {
      // given
      val model = sampleModel
      // when
      val actual = ProjectModelKnowledgeSet(model)
      // then
      actual.triples should contain(Triple(
        UriNode("java:SomeClass"),
        UriNode("d:belongsToLibrary"),
        UriNode("lib:mylib.jar"),
        true))
    }
    scenario("Class belongs to a Package") { pending }
    scenario("Method takes parameter") { pending }
    scenario("Method invokes") { pending }
    scenario("Method invokes static") { pending }
    scenario("Method creates instance") { pending }
  }

  def sampleModel = new LibraryModel(
    "mylib.jar",
    Seq(ClassModel("SomeClass", "eu.semantiq", Seq(), Seq(), "SuperClass", Seq())))
}