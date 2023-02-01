import sys
from awsglue.transforms import *
from awsglue.utils import getResolvedOptions
from awsglue.context import GlueContext
from awsglue.job import Job
from awsglue import DynamicFrame
from pyspark.context import SparkContext
from pyspark.sql import DataFrame, Row
from pyspark.sql.functions import col
import uuid


params = [
    'JOB_NAME',
    'TempDir',
    'src_kinesis_data_stream_arn',
    'dst_redshift_database_name',
    'dst_redshift_schema_name',
    'dst_redshift_table_name',
    'redshift_connection_name',
]

args = getResolvedOptions(sys.argv, params)
sc = SparkContext()
glueContext = GlueContext(sc)
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args["JOB_NAME"], args)

# Script generated for node Kinesis Stream
dataframe_KinesisStream_node1 = glueContext.create_data_frame.from_options(
    connection_type="kinesis",
    connection_options={
        "typeOfData": "kinesis",
        "streamARN": args["src_kinesis_data_stream_arn"],
        "classification": "json",
        "startingPosition": "latest",
        "inferSchema": "true",
    },
    transformation_ctx="dataframe_KinesisStream_node1",
)


def processBatch(data_frame, batchId):
    if data_frame.count() > 0:
        dynamic_frame = DynamicFrame.fromDF(
            data_frame, glueContext, "from_data_frame"
        )
        data_frame=dynamic_frame.toDF()
        data_frame.printSchema()
        data_frame.show(100)
        upsert_data_frame = data_frame.filter((col('eventName') == 'INSERT') | (col('eventName') == 'MODIFY')).select(col('dynamodb.NewImage.*'))
        dynamodb_data_dynamic_frame = DynamicFrame.fromDF(upsert_data_frame, glueContext, "from_data_frame")
        redshift_data_dynamic_frame = ApplyMapping.apply(
            frame=dynamodb_data_dynamic_frame,
            mappings=[
                ("pk.S", "string", "pk", "string"),
                ("sk.S", "string", "sk", "string"),
                ("value.S", "string", "value", "int"),
                ("updated_at.S", "string", "updated_at", "timestamp"),
            ],
            transformation_ctx="redshift_data_dynamic_frame",
        )
        redshift_data_dynamic_frame.printSchema()
        redshift_data_dynamic_frame.show(100)

        redshift_data_frame = redshift_data_dynamic_frame.toDF()
        redshift_data_frame = redshift_data_frame.orderBy(col("pk").asc(), col("sk").asc(), col("updated_at").asc())
        redshift_data_frame = redshift_data_frame.dropDuplicates(["pk", "sk", "updated_at"])
        redshift_data_dynamic_frame_no_duplicate = DynamicFrame.fromDF(redshift_data_frame, glueContext, "from_data_frame")
        redshift_data_dynamic_frame_no_duplicate.printSchema()
        redshift_data_dynamic_frame_no_duplicate.show(100)

        randomUuidStr = str(uuid.uuid4()).replace("-", "")
        stage_table_name = f"{args['dst_redshift_schema_name']}.{args['dst_redshift_table_name']}{randomUuidStr}"
        target_table_name = f"{args['dst_redshift_schema_name']}.{args['dst_redshift_table_name']}"
        pre_query = f"drop table if exists {stage_table_name};create table {stage_table_name} as select * from {target_table_name} where 1=2;"
        post_query = f"begin;delete from {target_table_name} using {stage_table_name} where {stage_table_name}.pk = {target_table_name}.pk and {stage_table_name}.sk = {target_table_name}.sk; insert into {target_table_name} select * from {stage_table_name}; drop table if exists {stage_table_name }; end;"

        RedshiftCluster_node2 = glueContext.write_dynamic_frame.from_jdbc_conf(
            frame=redshift_data_dynamic_frame_no_duplicate,
            catalog_connection= args["redshift_connection_name"],,
            connection_options={
                "database":  args["dst_redshift_database_name"],
                "dbtable": stage_table_name,
                "preactions": pre_query,
                "postactions": post_query,
            },
            redshift_tmp_dir=args['TempDir'],
            transformation_ctx="RedshiftCluster_node2",
        )

glueContext.forEachBatch(
    frame=dataframe_KinesisStream_node1,
    batch_function=processBatch,
    options={
        "windowSize": "60 seconds",
        "checkpointLocation": args["TempDir"] + "/" + args["JOB_NAME"] + "/checkpoint/",
    },
)
job.commit()
