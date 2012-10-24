organization := "eu.semantiq"

name := "design-validator"

publishTo := Some(Resolver.file("Design Validator Repository", new File("./snapshots")))

resolvers += "Semblade on GitHub" at "https://raw.github.com/michalj/semblade-core/master/releases"

libraryDependencies += "org.eclipse.jetty.orbit" % "org.objectweb.asm" % "3.3.1.v201105211655" from "http://repo1.maven.org/maven2/org/eclipse/jetty/orbit/org.objectweb.asm/3.3.1.v201105211655/org.objectweb.asm-3.3.1.v201105211655.jar"

libraryDependencies += "eu.semantiq" %% "semblade-core" % "1.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.1" % "test"
