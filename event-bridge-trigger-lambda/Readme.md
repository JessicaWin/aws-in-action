# Install nodejs

https://nodejs.org/en/download/

# Install serverless

https://www.npmjs.com/package/serverless

```bash
npm install -g serverless
```

# create deploy bucket

create a bucket with name 'com.${bucketNamePrefix}.deploy-bucket', bucketNamePrefix can be any value, you decide

# Deploy

## package

```bash
mvn package
```

## Run deploy command

update serverless.xml custom:bucketNamePrefix to your own bucket name
update serverless-manual.xml custom:bucketNamePrefix to your own bucket name

- serverless.yml is using AWS::Events::Rule and AWS::Lambda::Permission generate automatically by the serverless framework, if you want to have more contorl over the name for the event rule, you can use serverless-manual.xml, it use AWS::Events::Rule and AWS::Lambda::Permission specify in the serverless file.

```bash
sls deploy -c serverless.yml
```

# Ref

https://www.serverless.com/framework/docs/providers/aws/events/s3
https://docs.aws.amazon.com/AmazonS3/latest/userguide/NotificationHowTo.html
https://www.serverless.com/framework/docs/providers/aws/events/cloudwatch-event
