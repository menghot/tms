from __future__ import annotations

from datetime import datetime

from airflow import models
from airflow.providers.trino.operators.trino import TrinoOperator
from airflow.contrib.operators.spark_submit_operator import SparkSubmitOperator
from airflow.operators.bash import BashOperator

from airflow.providers.trino.hooks.trino import TrinoHook




# [START howto_operator_trino]

with models.DAG(
    dag_id="demo",
    schedule="@once",  # Override to match your needs
    start_date=datetime(2022, 1, 1),
    catchup=False,
    tags=["trino"],
) as dag:
    spark_task = SparkSubmitOperator(
        task_id='spark_operator_submit',
        conn_id='spark93',
        java_class='org.example.JavaSparkPi',
        application='hdfs://10.194.186.216:8020/tmp/demo-spark-iceberg-1.0-SNAPSHOT.jar',  # Path to your Spark application
        total_executor_cores='2',  # Number of cores for the job
        executor_cores='1',  # Number of cores per executor
        executor_memory='2g',  # Memory per executor
        name='your_spark_job',  # Name of the job
        verbose=True,
        conf={"spark.master.rest.enabled":"true"},  # Additional Spark configurations if needed
        dag=dag
    )


    spark_task_k8s = SparkSubmitOperator(
        task_id='spark_task_k8s',
        conn_id='spark_k8s',
        java_class='org.example.JavaSparkPi',
        application='hdfs://10.194.186.216:8020/tmp/demo-spark-iceberg-1.0-SNAPSHOT.jar',  # Path to your Spark application
        total_executor_cores='2',  # Number of cores for the job
        executor_cores='2',  # Number of cores per executor
        executor_memory='2g',  # Memory per executor
        name='spark-pi-job',  # Name of the job
        verbose=True,
        conf={"spark.kubernetes.container.image":"apache/spark:3.5.0"},  # Additional Spark configurations if needed
        dag=dag
    )


    run_this = BashOperator(
        task_id="spark_bash_submit",
        bash_command="spark-submit --master local --class org.example.JavaSparkPi hdfs://10.194.186.216:8020/tmp/demo-spark-iceberg-1.0-SNAPSHOT.jar",
    )

    trino_templated_query = TrinoOperator(
        task_id="trino_templated_query",
        sql=["drop table if exists iceberg.simon.ice_person_20w3", "create table iceberg.simon.ice_person_20w3 as SELECT * FROM iceberg.simon.ice_person_20w","delete from iceberg.simon.ice_person_20w where person_id=88888"],
        handler=list,
        trino_conn_id='trino_connection'
    )



    trino_multiple_queries = TrinoOperator(
        task_id="trino_multiple_queries",
        trino_conn_id='trino_connection',
        sql=f"""CREATE TABLE IF NOT EXISTS iceberg.simon.mytable(cityid bigint,cityname varchar)""",
        handler=list,
    )

    spark_task >> spark_task_k8s >> [run_this , trino_templated_query]  >> trino_multiple_queries
