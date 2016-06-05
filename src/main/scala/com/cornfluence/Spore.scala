package com.cornfluence

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

trait Spore {
   def run[T](implicit stream : RDD[T], ssc : SparkContext)
}
