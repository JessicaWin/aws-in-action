#!/bin/bash
echo "Deploying deploy bucket ..."
DELETE=$1

STAGE_LIST=( develop production )

for i in "${STAGE_LIST[@]}"
do
	STAGE=$i
	REGION="ap-northeast-1"
	if [ "$STAGE" = "develop" ];then
		REGION="ap-southeast-1"
	fi
	echo "Deploying deploy-bucket.yml to $STAGE in $REGION ..."
	if [ "$DELETE" = "remove" ];then
		aws cloudformation delete-stack --stack-name $STAGE-deploy-bucket
	else
		stackOutput=`aws cloudformation create-stack --stack-name $STAGE-deploy-bucket --template-body file://./deploy-bucket.yml --parameters ParameterKey=Stage,ParameterValue=$STAGE --region $REGION 2>&1`
        if [[ "$stackOutput" =~ "AlreadyExistsException" ]]; then
			noUpdate=`aws cloudformation update-stack --stack-name $STAGE-deploy-bucket --template-body file://./deploy-bucket.yml --parameters ParameterKey=Stage,ParameterValue=$STAGE --region $REGION 2>&1`
			if [[ "$noUpdate" =~ "No updates are to be performed" ]]; then
				echo "No updates are to be performed"
			else
				echo $noUpdate
			fi
		else
			echo $stackOutput
		fi
	fi
done
