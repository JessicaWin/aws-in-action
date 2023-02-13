#!/bin/bash
DELETE=$1
./deploy-single-resource.sh deploy-bucket $DELETE
echo "waitting 60 seconds for deploy bucket create finish..."
sleep 60
./deploy-single-resource.sh ecr $DELETE
./deploy-single-resource.sh iam $DELETE
./deploy-single-resource.sh vpc $DELETE
./deploy-single-resource.sh ecs $DELETE
./deploy-single-resource.sh github-iam $DELETE