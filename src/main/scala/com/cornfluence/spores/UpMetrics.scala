//package com.cornfluence.spores
//
//class UpMetrics extends Spore with Serializable {
//
//   def run(implicit stream: RDD[HealthData], ssc: SparkContext) = {
//
////      val summary: MultivariateStatisticalSummary = Statistics.colStats(stream)
////      println(summary.mean) // a dense vector containing the mean value for each column
////      println(summary.variance) // column-wise variance
////      println(summary.numNonzeros) // number of nonzeros in each column
////      val data = stream.cache()
////
////
////      val rows = data.map { dataItem =>
////         Vectors.dense(dataItem.m_steps.map(_.toDouble).toArray)
////      }
////      val mat: RowMatrix = new RowMatrix(rows)
////      mat.computeCovariance()
//   }
//}

