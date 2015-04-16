import sbtassembly.Assembly

name := "emas-labs"

version := "1.0"

scalaVersion := "2.11.5"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "pl.edu.agh.scalamas" %% "emas" % "0.2.0-SNAPSHOT"

libraryDependencies += "net.ceedubs" %% "ficus" % "1.1.1"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.13"

mainClass in assembly := None

assemblyOutputPath in assembly := file(s"target/${(mainClass in assembly).value.getOrElse("emas")}.jar")

val assemblyAll = taskKey[Seq[File]]("Builds a deployable fat jar for every mainClass in the project.")

val assemblyOutputDirectory = settingKey[String]("Output directory of the fat jar(s).")

val assemblyMainClasses = taskKey[Seq[String]]("The main classes to build into fat jars.")

assemblyOutputDirectory := "target"

assemblyMainClasses := (discoveredMainClasses in Compile).value

assemblyAll := {
  assemblyMainClasses.value.map {
    m =>
      val outputPath = file(s"${assemblyOutputDirectory.value}/$m.jar")
      val s = (streams in assembly).value
      Assembly(
        outputPath, (assemblyOption in assembly).value,
        (packageOptions in assembly).value, (assembledMappings in assembly).value,
        s.cacheDirectory, s.log)
  }
}

