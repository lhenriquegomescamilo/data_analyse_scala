package br.com.lhtecnologia.basics

import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Row, SparkSession}

object ComplexyStructure {
  def main(args: Array[String]): Unit = {
    implicit val spark: SparkSession = SparkSession.builder()
      .master("local")
      .appName("complexy_value")
      .getOrCreate()


    val schema = new StructType(Array(
      StructField("id", IntegerType, nullable = true),
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

    val dataComplex = dataHandler.select(struct("id", "job").alias("complexo"))
    dataComplex.show(5)
  }
}
