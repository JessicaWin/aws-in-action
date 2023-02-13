aws dynamodb update-continuous-backups \
    --table-name \
        develop.TestTableCli \
    --point-in-time-recovery-specification \
        PointInTimeRecoveryEnabled=true