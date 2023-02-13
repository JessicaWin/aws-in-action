# dynamodb-in-action
## create table
### create table with cloud formation
* cd dynamodb-in-action/create-table/src/main/resources/table/create/cloudformation
* sls deploy -v

### create table with cli
* cd dynamodb-in-action/create-table/src/main/resources/table/create/cli
* ./create-table.bash
* ./update-time-to-live.bash
* ./update-continuous-backups.bash
* ./update-contributor-insights.bash


### create table with java
* cd dynamodb-in-action/
* mvn eclipse:eclipse
