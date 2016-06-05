package com.cornfluence

import scala.util.Try

object SparkConfig {
   val appName = "Aegle"
   val conf = new SparkConfig("")
}

class SparkConfig(environment: String = "") {
   val rootConfig = new AegleConfig().rootConfig
   val checkpointDir = Try(rootConfig.getString(Config.checkpointDir)).toOption
   val spores = rootConfig.getStringList(Config.spores)
   val sparkMaster = Try(rootConfig.getString(Config.sparkMaster)).toOption
   val isClustered = Try(rootConfig.getBoolean(Config.isClustered)).toOption
   val influxHost = Try(rootConfig.getString(Config.influxHost)).toOption
   val influxPort = Try(rootConfig.getString(Config.influxPort)).toOption
   val influxSchema = Try(rootConfig.getString(Config.influxSchema)).toOption
}

object Config {
   val appName = "name"
   val influxHost = "influxHost"
   val influxPort = "influxPort"
   val influxSchema = "influxSchema"
   val checkpointDir = "checkpointDir"
   val spores = "spores"
   val sparkMaster = "sparkMaster"
   val isClustered = "isClustered"
}

import com.typesafe.config.ConfigFactory

class AegleConfig() {
   lazy val rootConfig = ConfigFactory.load("aegle").withFallback(ConfigFactory.load())
}