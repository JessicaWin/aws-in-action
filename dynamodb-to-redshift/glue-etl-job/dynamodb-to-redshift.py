import sys
from awsglue.transforms import *
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from awsglue.context import GlueContext
from awsglue.job import Job

params = [
    'JOB_NAME',
    'TempDir',
]

args = getResolvedOptions(sys.argv, params)
sc = SparkContext()
glueContext = GlueContext(sc)
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args["JOB_NAME"], args)

DynamoDBtable_node1 = glueContext.create_dynamic_frame.from_catalog(
    database="dynamodb-database",
    table_name="testsynctoredshift",
    transformation_ctx="DynamoDBtable_node1",
)

RedshiftCluster_node2 = glueContext.write_dynamic_frame.from_catalog(
    frame=DynamoDBtable_node1,
    database="redshift-database",
    table_name="dev_public_test_glue_etl_sync_to_redshift",
    redshift_tmp_dir=args["TempDir"],
    transformation_ctx="RedshiftCluster_node2",
)

job.commit()
