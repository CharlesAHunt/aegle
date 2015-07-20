package com.cornfluence.spores

import com.cornfluence.{HealthData, Spore}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.Duration
import org.apache.spark.streaming.dstream.DStream

class UpMetrics extends Spore with Serializable {

   def run(implicit stream : RDD[HealthData], ssc : SparkContext) = {
      val data = stream.cache()
         data.foreach { data =>
            println(data)
         }
   }
}