#!/bin/bash
echo "Deploying github iam role ..."
DELETE=$1

STAGE_LIST=( develop )

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
