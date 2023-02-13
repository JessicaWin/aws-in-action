aws dynamodb create-table \
    --table-name develop.TestTableCli \
    --attribute-definitions \
        AttributeName=pk,AttributeType=S \
        AttributeName=sk,AttributeType=S \
        AttributeName=gsiPk,AttributeType=S \
        AttributeName=gsiSk,AttributeType=S \
        AttributeName=gsiTwoPk,AttributeType=S \
        AttributeName=gsiTwoSk,AttributeType=S \
        AttributeName=lsiOneSk,AttributeType=S \
        AttributeName=lsiTwoSk,AttributeType=S \
    --key-schema \
        AttributeName=pk,KeyType=HASH \
        AttributeName=sk,KeyType=RANGE \
    --billing-mode \
        PROVISIONED \
    --provisioned-throughput \
        "{
            \"ReadCapacityUnits\": 5,
            \"WriteCapacityUnits\": 5
        }" \
    --local-secondary-indexes \
        "[
            {
                \"IndexName\": \"test.lsiOne\",
                \"KeySchema\": [{\"AttributeName\":\"pk\",\"KeyType\":\"HASH\"},
                                {\"AttributeName\":\"lsiOneSk\",\"KeyType\":\"RANGE\"}],
                \"Projection\":{
                    \"ProjectionType\":\"ALL\"
                }
            },
            {
                \"IndexName\": \"test.lsiTwo\",
                \"KeySchema\": [{\"AttributeName\":\"pk\",\"KeyType\":\"HASH\"},
                                {\"AttributeName\":\"lsiTwoSk\",\"KeyType\":\"RANGE\"}],
                \"Projection\":{
                    \"ProjectionType\":\"ALL\"
                }
            }
        ]" \
    --global-secondary-indexes \
        "[
            {
                \"IndexName\": \"test.gsi\",
                \"KeySchema\": [{\"AttributeName\":\"gsiPk\",\"KeyType\":\"HASH\"},
                                {\"AttributeName\":\"gsiSk\",\"KeyType\":\"RANGE\"}],
                \"Projection\":{
                    \"ProjectionType\":\"ALL\"
                },
                \"ProvisionedThroughput\": {
                    \"ReadCapacityUnits\": 5,
                    \"WriteCapacityUnits\": 5
                }
            },
            {
                \"IndexName\": \"test.gsiTwo\",
                \"KeySchema\": [{\"AttributeName\":\"gsiTwoPk\",\"KeyType\":\"HASH\"},
                                {\"AttributeName\":\"gsiTwoSk\",\"KeyType\":\"RANGE\"}],
                \"Projection\":{
                    \"ProjectionType\":\"ALL\"
                },
                \"ProvisionedThroughput\": {
                    \"ReadCapacityUnits\": 5,
                    \"WriteCapacityUnits\": 5
                }
            }
        ]" \
    --sse-specification \
        Enabled=false \
    --tags \
        "[
            {
                \"Key\": \"product\",
                \"Value\": \"social\"
            },
            {
                \"Key\": \"Name\",
                \"Value\": \"test.develop\"
            }  
        ]"