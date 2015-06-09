package com.cornfluence

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}
import org.json4s.native.Serialization._
import org.json4s.{NoTypeHints, native, Formats}

import scala.util.Try

object SporeHarbinger {

   def main(args: Array[String]) {
      val config = SparkConfig.conf
      import scala.collection.JavaConversions._
      val spores = config.spores.toList.map { sporeName =>
         Try(Class.forName(s"com.cornfluence.spark.streaming.spores.$sporeName").newInstance.asInstanceOf[Spore]).get
      }

      val sparkConf = new SparkConf().setAppName("Aegle").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.scheduler.mode", "FAIR").registerKryoClasses(Array(classOf[HealthData]))

      if(config.isClustered)
         sparkConf.setMaster(config.sparkMaster)

      implicit val sc = new SparkContext(sparkConf)

      val data = Array("", "", "", "", "")
      val stream = sc.parallelize(data)

      implicit val lines : RDD[HealthData] = stream.map(Parser.logParser)

      spores.foreach(_.run)
   }
}

object Parser {
   def logParser(json: String): HealthData = {
      implicit val jsonFormats: Formats = native.Serialization.formats(NoTypeHints)
      read[HealthData](json)
   }
}