package org.example;

import org.apache.spark.sql.SparkSession;

public class SimpleApp {
    public static void main(String[] args) {
        // Create a Spark session
        SparkSession spark = SparkSession
                .builder()
                .config("spark.master", "local")
                .appName("IcebergWriteExample")
                .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkCatalog")
                .config("spark.sql.catalog.spark_catalog.type", "hadoop")
                .config("spark.sql.catalog.spark_catalog.warehouse", "warehouse")
                .config("spark.sql.extensions","org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")

                .config("spark.extraListeners", "io.openlineage.spark.agent.OpenLineageSparkListener")
                .getOrCreate();


        spark.sql("create schema if not exists ods");
        spark.sql("drop table if exists ods.my_iceberg_table purge ");
        spark.sql("drop table if exists ods.my_iceberg_table2 purge ");
        spark.sql("create table IF NOT EXISTS ods.my_iceberg_table  (id int, data string) using iceberg");
        spark.sql("create table IF NOT EXISTS ods.my_iceberg_table2 (id int, data string) using iceberg");
        spark.sql("MERGE INTO ods.my_iceberg_table  t using ods.my_iceberg_table2 s on t.id = s.id when matched then update set t.data = s.data");

        // Stop the Spark session
        spark.stop();
    }
}
