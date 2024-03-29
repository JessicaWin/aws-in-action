# pre request

- install node: https://nodejs.org/en/
- install serverless framework: https://www.npmjs.com/package/serverless

# Architercure

![Architercure](aurora-to-redshift-vpc.png)

# create deploy bucket

replac the ${bucketNamePrefix} to any value for ParameterValue=${bucketNamePrefix} in below command, bucketNamePrefix can be any value, you decide, eg. ParameterValue=jessica

```
aws cloudformation create-stack --stack-name deploy-bucket-setup --template-body file://./deploy-bucket.yml --parameters ParameterKey=bucketNamePrefix,ParameterValue=${bucketNamePrefix} --region ap-southeast-1
```

# deploy vpc

update vpc.xml resources:Parameters:MyIP to your own ip address
update vpc.xml custom:bucketNamePrefix to your bucketNamePrefix

```
sls deploy -c vpc.yml
```

# deploy aurora

update aurora.xml custom:bucketNamePrefix to your bucketNamePrefix

```
sls deploy -c aurora.yml
```

# deploy redshift

update redshift.xml custom:bucketNamePrefix to your bucketNamePrefix

```
sls deploy -c redshift.yml
```

# deploy dms

```
sls deploy -c dms.yml
```

# insert data to aurora

- connect to aurora
- create table and insert data

```
CREATE TABLE person (
    person_id int,
    last_name varchar(255),
    first_name varchar(255),
    ciry varchar(255)
);
INSERT INTO person VALUES (1, 'win', 'jessica', 'sh');
```

# sync data from aurora to redshift

- run dms database migration task
- check redshift with redshift query editor

# Troubleshoot

[Why is my AWS DMS task in an error status](https://aws.amazon.com/premiumsupport/knowledge-center/dms-task-error-status/#:~:text=Short%20description-,An%20AWS%20DMS%20task%20that%20is%20in%20an%20error%20status,task%20stops%20with%20fatal%20errors)
[Why can't I see CloudWatch Logs for an AWS DMS task?](https://aws.amazon.com/premiumsupport/knowledge-center/dms-cloudwatch-logs-not-appearing/)
[How do I turn on, access, or delete CloudWatch logs for AWS DMS?](https://aws.amazon.com/premiumsupport/knowledge-center/manage-cloudwatch-logs-dms/)
