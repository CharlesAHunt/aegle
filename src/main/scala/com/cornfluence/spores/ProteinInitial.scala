package com.cornfluence.spores

import com.cornfluence.Spore
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.biojava.nbio.structure.{StructureIO, StructureTools}
import scala.collection.JavaConversions._

class ProteinInitial extends Spore with Serializable {

  def run[T](implicit stream: RDD[T], ssc: SparkContext) = {

    val structure = StructureIO.getStructure("4HHB")
    // and let's print out how many atoms are in this structure
    val k = StructureTools.getNrAtoms(structure)
    val chains = structure.getChains()

    val kPar = ssc.parallelize(chains)

    kPar.map(_.getAtomLength).stats().mean

    kPar.saveAsTextFile("hdfs://localhost:9000/user/bio")
  }
}

case class ProteinData()