package xdean.stackoverflow

import _root_.java.text.SimpleDateFormat
import _root_.java.util.Date

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object Q64374547 {

  case class Event(event: String, consumer: Int, timestamp: String)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[4]").getOrCreate()

    import spark.implicits._

    val df = spark.createDataFrame[Event](Seq(
      Event("E", 1, "2020-09-09 13:15:00"),
      Event("E", 1, "2020-09-09 13:30:00"),
      Event("E", 1, "2020-09-09 14:20:00"),
      Event("T", 1, "2020-09-09 14:35:00"),
      Event("T", 2, "2020-09-09 13:20:00"),
      Event("E", 2, "2020-09-09 13:25:00"),
      Event("E", 2, "2020-09-09 14:45:00"),
      Event("T", 2, "2020-09-09 14:50:00")
    )).withColumn("timestamp", $"timestamp")

    val findSessionStartTime = udf((rows: Seq[Seq[Any]]) => {
      val parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

      var result: Date = null
      for (row <- rows.reverse) {
        val event = row(0)
        val time = parser.parse(row(1).toString)
        if (event == "E") {
          if (result == null || result.getTime - time.getTime < 3600000) {
            result = time
          }
        }
      }
      if (result == null)
        null
      else
        parser.format(result)
    })

    df.withColumn("events", collect_list(array($"event", $"timestamp")).over(Window
      .partitionBy($"consumer")
      .orderBy($"timestamp")))
      .withColumn("session_timestamp", to_timestamp(findSessionStartTime($"events")))
      .drop("events")
      .show(false)
  }
}
