package com.cornfluence

import org.apache.spark.SparkContext
import org.apache.spark.streaming.dstream.DStream

trait Spore {
   def run(implicit stream : DStream[HealthData], ssc : SparkContext)
}
