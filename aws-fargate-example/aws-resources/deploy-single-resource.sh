#!/bin/bash
echo "Deploying deploy bucket ..."
RESOURCE_FOLDER=$1
DELETE=$2

STAGE_LIST=( develop production )

cd $RESOURCE_FOLDER
if [ -f "deploy.sh" ];then
    echo "Deploy with $RESOURCE_FOLDER/deploy.sh"
    chmod 777 deploy.sh
    ./deploy.sh $DELETE
else
    echo "Deploy with deploy-single-resource.sh"
    for i in "${STAGE_LIST[@]}"
    do
        STAGE=$i
        REGION="ap-northeast-1"
        if [ "$STAGE" = "develop" ];then
            REGION="ap-southeast-1"
        fi
        echo "Deploying $RESOURCE_FOLDER to $STAGE in $REGION ..."
        if [ "$DELETE" = "remove" ];then
            sls remove --stage ${STAGE} --region ${REGION}
        else
            sls deploy --stage ${STAGE} --region ${REGION}
        fi
    done
fi
cd -