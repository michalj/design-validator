package designvalidator.graph.graphviz

import org.scalatest.TestPendingException
import scala.sys.process._

trait PendingHelper {
  def pendingUnlessDotAvailable(f: => Unit) {
    try {
      "dot --help" !
    } catch {
      case e: Exception => throw new TestPendingException() 
    }
    f
  }
}