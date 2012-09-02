package designvalidator.model.n3

import designvalidator.model._
import eu.semantiq.semblade._

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
    Seq()
  }
  
  private def describeLibrary(model: LibraryModel) = {
    model.classes map (c => Triple(
        UriNode("java:" + c.name),
        UriNode("d:belongsToLibrary"),
        UriNode("lib:" + model.name), true))
  }
  
}