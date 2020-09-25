# Glacier Upload Helper

## Compilation
Run `mvn package`

## Usage Example
Run `java -jar glacier-1.0-SNAPSHOT-jar-with-dependencies.jar --vault-name {vaultName} --description {description} --input-file-path {pathOffileToArchive} --account-id {awsAccountId}`

Save the archiveId at the end of the upload for future use.
If archiveId is lost, you will need to retrieve the inventory listing using aws-cli and is a 2-step process:
1. Run `aws glacier initiate-job --job-parameters '{"Type": "inventory-retrieval"}' --account-id {awsAccountId} --vault-name {vaultName}`
This starts a job to retrieve the inventory. Save the jobId for the next step. 
2. Run `aws glacier get-job-output --account-id {awsAccountId} --vault-name {vaultName} --job-id {jobId} {fileToBeSaved}`
This saves the inventory to your file system. Note that it might take a day for the inventory list to be ready.


