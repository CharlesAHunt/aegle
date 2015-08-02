package com.cornfluence.spores

import com.cornfluence.{HealthData, Spore}
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary, Statistics}
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.{Vectors, Vector}
import org.apache.spark.streaming.Duration
import org.apache.spark.streaming.dstream.DStream

class UpMetrics extends Spore with Serializable {

   def run(implicit stream: RDD[HealthData], ssc: SparkContext) = {

      val summary: MultivariateStatisticalSummary = Statistics.colStats(stream)
      println(summary.mean) // a dense vector containing the mean value for each column
      println(summary.variance) // column-wise variance
      println(summary.numNonzeros) // number of nonzeros in each column
      val data = stream.cache()


      val rows = data.map { dataItem =>
         Vectors.dense(dataItem.m_steps.map(_.toDouble).toArray)
      }
      val mat: RowMatrix = new RowMatrix(rows)
      mat.computeCovariance()
   }
}

