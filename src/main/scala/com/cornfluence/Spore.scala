package com.cornfluence

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

trait Spore {
   def run(implicit stream : RDD[HealthData], ssc : SparkContext)
}
