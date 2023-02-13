aws dynamodb update-time-to-live \
    --table-name \
        develop.TestTableCli \
    --time-to-live-specification \
        Enabled=true,AttributeName=ttl