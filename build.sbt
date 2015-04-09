name := "emas-labs"

version := "1.0"

scalaVersion := "2.11.5"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "pl.edu.agh.scalamas" %% "emas" % "0.2.0-SNAPSHOT"

libraryDependencies += "net.ceedubs" %% "ficus" % "1.1.1"

assemblyOutputPath in assembly := file("target/emas-discrete.jar")