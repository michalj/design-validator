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
        UriNode("class:eu.semantiq.SomeClass"),
        UriNode("d:belongsToLibrary"),
        UriNode("lib:mylib.jar"),
        true))
    }
    scenario("Library belongs to App") {
      // given
      val model = AppModel("SampleApp.war", Seq(sampleModel))
      // when
      val actual = ProjectModelKnowledgeSet(model)
      // then
      actual.triples should contain(Triple(
        UriNode("lib:mylib.jar"),
        UriNode("d:belongsToApp"),
        UriNode("app:SampleApp.war"),
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