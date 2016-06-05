package com.cornfluence

import com.cornfluence.spores.ProteinData
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.json4s.native.Serialization._
import org.json4s.{Formats, NoTypeHints, native}

import scala.util.Try

object SporeHarbinger {

   def main(args: Array[String]) {
      val config = SparkConfig.conf
      import scala.collection.JavaConversions._
      val spores = config.spores.toList.map { sporeName =>
         Try(Class.forName(s"com.cornfluence.spores.$sporeName").newInstance.asInstanceOf[Spore]).get
      }

      val sparkConf = new SparkConf().setAppName("Aegle").
        set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").
        set("spark.scheduler.mode", "FAIR").
        registerKryoClasses(Array(classOf[ProteinData]))

//      if(config.isClustered)
//         sparkConf.setMaster(config.sparkMaster)

      implicit val sc = new SparkContext(sparkConf)

      val data = sc.textFile("hdfs://localhost:9000/charles/aegle/up3.csv")

      implicit val lines : RDD[ProteinData] = data.map(i => ProteinData())

      spores.foreach(_.run[ProteinData])
   }
}