package br.com.lhtecnologia.basics

import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object Basics {
  def main(args: Array[String]): Unit = {
    implicit val spark: SparkSession = SparkSession.builder().master("local").appName("BasicsScala").getOrCreate()
    val data = loadData("src/data/bank-additional-full.csv")

    val schema = new StructType(Array(
      StructField("age", IntegerType, nullable = true),
      StructField("job", StringType, nullable = true)
    ))

    val newRows = Seq(
      Row(30, "Cientista de dados"),
      Row(20, "Dev Java"),
      Row(10, null)
    )

    val parallelizedRows = spark.sparkContext.parallelize(newRows)

    val dataHandler = spark.createDataFrame(parallelizedRows, schema)
    dataHandler.show()

    import spark.sqlContext.implicits._
    data.select($"age", $"marital").na.drop("any")

    data.select($"age", $"marital").na.drop("all")

    dataHandler.na.fill("unknown").show()

    val valueToFill = Map("age" -> 0, "job" -> "unknown")
    dataHandler.na.fill(valueToFill).show()

    dataHandler.na.replace("job", Map(" Dev Java" -> "Desenolvedor")).show()

  }

  private def loadData(path: String)(implicit spark: SparkSession): DataFrame = {
    spark.read.option("header", "true")
      .option("inferSchema", "true")
      .option("delmiter", ";")
      .format("csv")
      .load(path)
  }
}
