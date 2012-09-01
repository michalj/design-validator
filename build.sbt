organization := "eu.semantiq"

name := "design-validator"

publishTo := Some(Resolver.file("Design Validator Repository", new File("./snapshots")))

libraryDependencies += "org.eclipse.jetty.orbit" % "org.objectweb.asm" % "3.3.1.v201105211655" from "http://repo1.maven.org/maven2/org/eclipse/jetty/orbit/org.objectweb.asm/3.3.1.v201105211655/org.objectweb.asm-3.3.1.v201105211655.jar"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.1" % "test"