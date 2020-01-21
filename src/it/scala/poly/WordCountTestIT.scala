package poly

import org.apache.spark.sql.{Dataset, SparkSession}
import org.scalatest.FlatSpec
import poly.storage.{ChatLog, Storage}
import poly.test.SharedSparkSession.spark
import thw.vancann.storage.ChatLog

class WordCountTestIT extends FlatSpec {

  class LocalStorage(spark: SparkSession) extends Storage {
    import spark.implicits._

    override def chatLogs: Dataset[ChatLog] = spark
      .read
      .option("header", "true")
      .option("delimiter", ";")
      .csv("src/it/resources/logs.csv")
      .as[ChatLog]

    override def writeToParquet(ds: Dataset[_], location: String): Unit = {}
  }

  "wordcount" should "read and write to disk" in {
    val config = UsageConfig("2017-05-25")
    WordCount.run(spark, config, new LocalStorage(spark))
  }

}
