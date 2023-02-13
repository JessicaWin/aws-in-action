# Case1：AWS Kinesis Data Firehose with http endpoint

## Arhitecture

![Architercure](firehose-http.png)

We use lambda function with api gateway trigger as http endpoint in this example.

## How to deploy

```bash
./deploy.sh http-destination
```

## How to test

1. invoke producer lambda with aws console or aws cli
2. check the consumer lambda log with aws console

# Case2：AWS Kinesis Data Firehose with s3

## Arhitecture

![Architercure](firehose-s3.png)

## How to deploy

```bash
./deploy.sh s3-destination
```

## How to test

1. invoke producer lambda with aws console or aws cli
2. check the object in s3 bucket
