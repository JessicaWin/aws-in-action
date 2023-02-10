import sys
from awsglue.transforms import *
from awsglue.utils import getResolvedOptions
from awsglue.context import GlueContext
from awsglue.job import Job
from awsglue import DynamicFrame
from pyspark.context import SparkContext
from pyspark.sql import DataFrame, Row
from pyspark.sql.functions import col

params = [
    'JOB_NAME',
    'TempDir',
    'src_kinesis_data_stream_arn',
    'des_glue_database_name',
    'des_glue_table_name'
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
        insert_data_frame = data_frame.filter((col('eventName') == 'INSERT')).select(col('dynamodb.NewImage.*'))
        dynamodb_data_dynamic_frame = DynamicFrame.fromDF(insert_data_frame, glueContext, "from_data_frame")
        redshift_data_dynamic_frame = ApplyMapping.apply(
            frame=dynamodb_data_dynamic_frame,
            mappings=[
                ("pk.S", "string", "pk", "string"),
                ("sk.S", "string", "sk", "string"),
                ("value.N", "string", "value", "int"),
                ("updated_at.S", "string", "updated_at", "timestamp"),
            ],
            transformation_ctx="redshift_data_dynamic_frame",
        )
        redshift_data_dynamic_frame.printSchema()
        redshift_data_dynamic_frame.show(100)

        RedshiftCluster_node2 = glueContext.write_dynamic_frame.from_catalog(
            frame=redshift_data_dynamic_frame,
            database=args["des_glue_database_name"],
            table_name=args["des_glue_table_name"],
            redshift_tmp_dir=args["TempDir"],
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
