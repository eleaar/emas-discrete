name := "emas-labs"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies += "pl.edu.agh.scalamas" %% "emas" % "0.1"

assemblyOutputPath in assembly := file("target/emas-discrete.jar")