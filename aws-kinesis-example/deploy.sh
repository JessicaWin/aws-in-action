#!/bin/bash
RESOURCE_FOLDER=$1
DELETE=$2

cd $RESOURCE_FOLDER
if [ -f "deploy.sh" ];then
    echo "Deploy with $RESOURCE_FOLDER/deploy.sh"
    chmod 777 deploy.sh
    ./deploy.sh $DELETE
else
    if [ -f "package.json" ];then
        npm install
    fi
    STAGE="develop"
    REGION="ap-southeast-1"
    echo "Deploying $RESOURCE_FOLDER to $STAGE in $REGION ..."
    if [ "$DELETE" = "remove" ];then
        sls remove --stage ${STAGE} --region ${REGION}
    else
        sls deploy --stage ${STAGE} --region ${REGION}
    fi
fi
cd -