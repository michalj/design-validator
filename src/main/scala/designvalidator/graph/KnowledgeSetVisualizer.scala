package designvalidator.graph

import eu.semantiq.semblade._
import designvalidator.model.ProjectModel
import designvalidator.model.n3.ProjectModelKnowledgeSet
import designvalidator.model.n3.DesignOntology
import eu.semantiq.semblade.ontologies._
import DefaultImplicits._

// TODO: customize the content of visualization (verticles: Seq[UriNode], edges: Seq[UriNode])
class KnowledgeSetVizualizer(filterVerticles: Seq[String]) extends IVisualizer {
	def apply(model: ProjectModel) = {
		val ks = new MemoryKnowledgeBase() +
		  ProjectModelKnowledgeSet(model) +
		  DesignOntology !
		val verticles = (ks ? "?x ?anyProperty ?anyObject")
		  .map(_("x").toString)
		  .filter(filterVerticles contains _)
		  .map(node => Verticle(node.toString, node.toString))
		println("verticles: " + verticles)
		val verticleIds = verticles map (_.id) toSet
		val edges = (ks ? "?x ?prop ?y")
		  .map(r => Edge(r("x").toString, r("y").toString, r("prop").toString))
		  .filter(e => verticleIds contains e.fromId)
		Graph("RDF Graph", Seq(), verticles.toSet, edges.toSet)
	}
}