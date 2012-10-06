package designvalidator.model.n3

import eu.semantiq.semblade._

object DesignImplicits extends Implicits {
  def prefixStore = DefaultPrefixStore ++ Map("d" -> "http://semantiq.eu/ontologies/design/1.0/")
}
