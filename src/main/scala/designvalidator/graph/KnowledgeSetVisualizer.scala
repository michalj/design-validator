package designvalidator.graph

import eu.semantiq.semblade._
import designvalidator.model.ProjectModel
import designvalidator.model.n3.ProjectModelKnowledgeSet
import designvalidator.model.n3.DesignOntology
import eu.semantiq.semblade.ontologies._
import DefaultImplicits._

// TODO: customize the content of visualization (verticles: Seq[UriNode], edges: Seq[UriNode])
class KnowledgeSetVizualizer extends IVisualizer {
	def apply(model: ProjectModel) = {
		val ks = new MemoryKnowledgeBase() +
		  ProjectModelKnowledgeSet(model) +
		  DesignOntology + RDFS + OWL + SEM !
		val verticles = (ks ? "?x ?anyProperty ?anyObject")
		  .map(_("x"))
		  .map(node => Verticle(node.toString, node.toString))
		val edges = (ks ? "?x ?prop ?y")
		  .map(r => Edge(r("x").toString, r("y").toString, r("prop").toString))
		Graph("RDF Graph", Seq(), verticles.toSet, edges.toSet)
	}
}