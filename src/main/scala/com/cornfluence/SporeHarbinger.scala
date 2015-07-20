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
         Try(Class.forName(s"com.cornfluence.spores.$sporeName").newInstance.asInstanceOf[Spore]).get
      }

      val sparkConf = new SparkConf().setAppName("Aegle").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.scheduler.mode", "FAIR").registerKryoClasses(Array(classOf[HealthData]))

//      if(config.isClustered)
//         sparkConf.setMaster(config.sparkMaster)

      implicit val sc = new SparkContext(sparkConf)

      val data = sc.textFile("hdfs://localhost:9000/charles/aegle/up3.csv")

      implicit val lines : RDD[HealthData] = data.map(Parser.logParser)

      spores.foreach(_.run)
   }
}

object Parser {
   def logParser(json: String): HealthData = {
      implicit val i = json.replaceAll(",+",",").split(",").toList
      HealthData(a(0),a(1),a(2),a(3),a(4),a(5),a(6),a(7),a(8),a(9),a(10),a(11),a(12),a(13),a(14),a(15),a(16),a(17),
         a(18),a(19),a(20),a(21),a(22),a(23),a(24),a(25),a(26),a(27),a(28),a(29),a(30),a(31),a(32),a(33),a(34),a(35),a(36),
         a(37),a(38),a(39),a(40),a(41),a(42),a(43),a(44),a(45),a(46),a(47),a(48),a(49),a(50),a(51),a(52),a(53),a(54),
         a(55),a(56),a(57),a(58),a(59),a(60),a(61),a(62),a(63),a(64),a(65),a(66),a(67),a(68),a(69),a(70),a(71),a(72))
   }

   def a(e : Int)(implicit arr : List[String]) = Try(arr(e)).toOption
}