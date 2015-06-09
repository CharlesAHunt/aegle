package com.cornfluence

object SparkConfig {
   val conf = new SparkConfig("")
}

class SparkConfig(environment: String = "") {
   val rootConfig = new AegleConfig().rootConfig
   val appName = "Aegle"
   val checkpointDir = rootConfig.getString(Config.checkpointDir)
   val spores = rootConfig.getStringList(Config.spores)
   val sparkMaster = rootConfig.getString(Config.sparkMaster)
   val isClustered = rootConfig.getBoolean(Config.isClustered)
   val influxHost = rootConfig.getString(Config.influxHost)
   val influxPort = rootConfig.getString(Config.influxPort)
   val influxSchema = rootConfig.getString(Config.influxSchema)
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
   lazy val rootConfig = ConfigFactory.load("spark").withFallback(ConfigFactory.load())
}