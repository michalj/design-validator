package designvalidator.model.n3

import java.io.File
import eu.semantiq.semblade._
import designvalidator.model._

object N3Dump extends App {
  	val jarFile = new File(args(1))
	val kb = new MemoryKnowledgeBase() + ProjectModelKnowledgeSet(ModelExtractor(jarFile)) !
	
	kb.dump.foreach(println)
}