import sbt._
import sbt.Keys._

import sbtassembly.Plugin.AssemblyKeys._

import scala.io.Source

object Resolvers {
   val resolversList = Seq(
      "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
      "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases/",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Typesafe Repository 2" at "http://repo.typesafe.com/typesafe/maven-releases/",
      "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"
      )
   }

object Dependencies {

   def compile(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
   def provided(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
   def test(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")

   val sparkVersion = "1.4.0"
   val spark_streaming = "org.apache.spark" %% "spark-streaming" % sparkVersion
   val spark_core = "org.apache.spark" %% "spark-core" % sparkVersion
   val spark_mlib =  "org.apache.spark" %% "spark-mllib" % sparkVersion
   val hadoop_client = "org.apache.hadoop" % "hadoop-client" % "2.6.0"
   val json4sNative = "org.json4s" %% "json4s-native" % "3.2.11"
   val json4sJackson = "org.json4s" %% "json4s-jackson" % "3.2.11"
   val joda = "joda-time" % "joda-time" % "2.7"
   val dispatch = "net.databinder.dispatch" %% "dispatch-core" % "0.11.2"
}

object BuildSettings {
   import Resolvers._

   val buildSettings = Seq(
      name := "aegle",
      organization := "com.cornfluence",
      version := "0.1.0",
      scalaVersion := "2.11.4",
      crossPaths := false,
      resolvers ++= resolversList
   )
}


object SparkBuild extends Build {
   import BuildSettings.buildSettings
   import Dependencies._
   import sbtassembly.Plugin.MergeStrategy

   val description = """Aegle""".stripMargin

   override lazy val  settings = super.settings ++ buildSettings :+ {
      shellPrompt := { state => scala.Console.YELLOW + "[" + scala.Console.CYAN + Project.extract(state).currentProject.id + scala.Console.YELLOW + "]" + scala.Console.RED + " $ " + scala.Console.RESET }
   }

   lazy val core = Project("aegle", file("."),
      settings = buildSettings ++
         sbtassembly.Plugin.assemblySettings ++
         addArtifact(Artifact("aegle", "assembly"), sbtassembly.Plugin.AssemblyKeys.assembly) ++
         Seq(
            mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
            {
                case meta if meta.contains("com") => MergeStrategy.last
                case meta if meta.contains("org") => MergeStrategy.last
                case meta if meta.contains("akka") => MergeStrategy.last
                case x => old(x)
            }},
            assemblyOption in assembly ~= { _.copy(includeScala = false) },
            publishArtifact in packageSrc := false,
            publishArtifact in packageDoc := false,
            publishArtifact in Test := false,
            libraryDependencies ++=
               compile(spark_core,spark_mlib,spark_streaming,json4sNative, json4sJackson,joda,hadoop_client,dispatch)
         )
   )

}
