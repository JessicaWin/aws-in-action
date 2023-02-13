## Description

[AWS ECS Fargate](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/AWS_Fargate.html) example repository.
[Nest](https://github.com/nestjs/nest) framework TypeScript starter project.

## Pre-request

node version: 18.0.0

```bash
$ brew install asdf
$ echo -e "\n. $(brew --prefix asdf)/libexec/asdf.sh" >> ~/.bash_profile
$ echo -e "\n. $(brew --prefix asdf)/etc/bash_completion.d/asdf.bash" >> ~/.bash_profile
$ source ~/.bash_profile
$ asdf plugin add nodejs https://github.com/asdf-vm/asdf-nodejs.git
$ asdf list all nodejs
$ asdf install nodejs 18.0.0
$ asdf global nodejs 18.0.0
```

## Installation

```bash
$ npm install
```

## Running the app

```bash
# development
$ npm run start

# watch mode
$ npm run start:dev

# production mode
$ npm run start:prod
```

## Test

```bash
# unit tests
$ npm run test

# e2e tests
$ npm run test:e2e

# test coverage
$ npm run test:cov
```

## Build docker image

```bash
$ docker build -t aws-fargate-example:latest --target=development ./

# use ocker-compose to build image for one service/stage
$ docker-compose build dev
$ docker-compose build prod

# use ocker-compose to build image for all services/stages
$ docker-compose build
```

## Run docker image

```bash
$ docker run -it -d -p 3000:3000 aws-fargate-example:latest

# use ocker-compose to start specific service
$ docker-compose up dev
$ docker-compose up prod
```

## Manual deploy ecs fargate to aws

### step1: deploy ecr
```bash
cd aws-resources
chmod 777 deploy-single-resource.sh
./deploy-single-resource.sh deploy-bucket
sleep 60
./deploy-single-resource.sh ecr
cd ../
```

### step2: build docker image and push to ecr
```bash
# replace ${AWS_AccountId} with your own aws accountId 
# Retrieve an authentication token and authenticate your Docker client to your registry.
$ aws ecr get-login-password --region ap-southeast-1 | docker login --username AWS --password-stdin ${AWS_AccountId}.dkr.ecr.ap-southeast-1.amazonaws.com
 
# Build your Docker image using the following command. 
$ docker-compose build dev
 
#After the build completes, tag your image so you can push the image to this repository:
$ docker tag aws-fargate-example-dev:latest ${AWS_AccountId}.dkr.ecr.ap-southeast-1.amazonaws.com/aws-fargate-example:latest 
 
#Run the following command to push this image to your newly created AWS repository:
$ docker push ${AWS_AccountId}.dkr.ecr.ap-southeast-1.amazonaws.com/aws-fargate-example:latest
```

### step3: deploy aws resources
```bash
cd aws-resources
chmod 777 deploy-single-resource.sh
./deploy-single-resource.sh iam
./deploy-single-resource.sh vpc
./deploy-single-resource.sh ecs
cd ../
```
After all deploy finish, you can access with your load balancer dns name.

## Auto deploy prepare

### step1: fork repository

Fork https://github.com/JessicaWin/aws-fargate-example to create you own repository

### step2: create github OIDCProvider and related role

```bash
cd aws-resources
chmod 777 deploy-single-resource.sh
./deploy-single-resource.sh github-iam
cd ../
```

### step3: add secrets to your own repository

- AWS_FEDARATED_ROLE：arn:aws:iam::${AWS_AccountId}:role/GitHubRole
- DEVELOP_ECS_TASK_ROLE: arn:aws:iam::${AWS_AccountId}:role/develop_ECSTaskRole
- PRODUCTION_ECS_TASK_ROLE:  arn:aws:iam::${AWS_AccountId}:role/production_ECSTaskRole

## Auto deploy ecs fargate task to develop env on push to master

- step1 clone your own repository
- step2 make any change to your code, commit and push to master branch
- step3 check the Develop Release & Deploy workflow under the Actions tab of your github repository

## Auto deploy ecs fargate task to production env on release

- step1 create a rlease to your own repository
- step2 check the Production Release & Deploy workflow under the Actions tab of your github repository

## Architercure

![Architercure](aws-fargate-example-architecture.png)

## AWS Resources

### deployment bucket

bucket used to save cloudformation template

- com.jessica.${Stage}-deploy-bucket

### iam

- github OIDCProvider
- GitHubRole: role & policy used by github workflow
- ECS Fargate TaskRole
- ECS Fargate ExecuteRole
- ECS Fargate AutoScalingRole

### VPC

- OneA vpc with three avaliability zones
- One public subnet for each availability
- One private subnet for each availability
- One internet gateway
- One nat gateway
- One security group for public subnet
- One security group for private subnet
- One access control list for public subnet
- One access control list for private subnet
- One route table for public subnet
- One route table for private subnet

### ecr

- aws-fargate-example repository to save docker image

### ecs

- ecs fargate cluster
- ecs fargate service
- ecs fargate task definition
- auto scaling policy
- auto scaling target
- application load balancer
- application load balancer target group
- application load balancer listener

## Manage AWS Resources

- develop stage will deploy to ap-aoutheast-1 region
- production stage will deploy to ap-northeast-1 region

### deploy one resource folder

```bash
cd aws-resources
chmod 777 deploy-single-resource.sh
#replace ${resource_folder} with one of following values: deploy-bucket, ecr, ecs, github-iam, iam, vpc
./deploy-single-resource.sh ${resource_folder}
```

### remove all

```bash
cd aws-resources
chmod 777 deploy-all.sh remove
./deploy-all.sh remove
```

### remove one resource folder

```bash
cd aws-resources
chmod 777 deploy-single-resource.sh
#replace ${resource_folder} with one of following values: deploy-bucket, ecr, ecs, github-iam, iam, vpc
./deploy-single-resource.sh ${resource_folder} remove
```
