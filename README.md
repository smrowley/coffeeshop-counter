# Coffeeshop Counter Service

This project implements an event driven Counter microservice for the Quarkus Coffeeshop project

## Building and running the service

### Packaging the application
Run the folllowing to build and package the application and confirm unit tests and coverage are passing:
```
./mvnw package
```

### Running the application in dev mode
You can run the application locally in dev mode (using in memory Kafka) with this command:
```
./mvnw quarkus:dev
```
Because the dev profile is using in-memory kafka, this service cannot be tested locally other than via unit tests.

## OpenShift Deployment
Export the following environment variables to use for your project.

```
OPENSHIFT_NAMESPACE=quarkuscoffee
```

### Create the Required infrastructure

#### Create Openshift Namespace

If needed, create the Openshift Namespace where you will deploy Counter.

```
oc new-project $OPENSHIFT_NAMESPACE
```

#### DynamoDb Table and Role
The commands in this section will create the prerequisite DynamoDb table and role needed to run the application prior deploying to Openshift.

Apply the required prerequisite OCP items, pipelines, and tasks in Openshift.
```
oc apply -f iac/openshift/pipeline/prereq/ocp/ -f iac/openshift/pipeline
```

Create the IAM IRSA role required for the Tekton Pipeline
```
aws cloudformation create-stack --stack-name quarkuscoffee-counter-iac-tekton-sa-stack --template-body file://iac/openshift/pipeline/prereq/aws/OpenshiftSaRoleCFT.yml --capabilities CAPABILITY_NAMED_IAM
```

Use the Tekton Pipeline to create the DynamoDb table and role.  The Tekton cli can be used to start the run.  The tkn command can be found by downloading the cli through the Openshift Console.  In the top right corner, click the `?`, then Command Line tools.

NOTE: The Openshift Pipelines UI currently has no way to set service accounts per task when starting a pipeline run, so this pipeline must be started either with the `tkn` cli or by using `oc apply` on PipelineRun file.  An example PipelineRun is provided at `iac/openshift/pipeline/test/pipelinerun.yml` for testing purposed.
```
tkn pipeline start counter-iac-aws-pipeline \
  --param git-repo-url=https://git.delta.com/ccoe/implementation-patterns-wip/coffeeshop/counter.git \
  --param git-revision=main \
  --param context-dir=iac/aws/ \
  --param template-file=counter_DynamoDb.yaml \
  --param parameters-file=counter_DynamoDb_parameters.json \
  --param capabilities-file=counter_DynamoDb_capabilities.json \
  --workspace name=source-workspace,claimName=counter-iac-aws-workspace \
  --task-serviceaccount validate-template=quarkuscoffee-counter-iac-aws-sa \
  --task-serviceaccount execute-template=quarkuscoffee-counter-iac-aws-sa 
```

Your DynamoDb table and client role should now be created.  The service account can be applied to the deployment if not already done.

#### Create the MSK Cluster
If not already prepared, create a new MSK Cluster by following the directions at https://git.delta.com/ccoe/implementation-patterns-wip/coffeeshop/common-infrastructure

#### Create the Infrastructure required in Openshift
Install infrastructure components by following instructions from: https://git.delta.com/ccoe/implementation-patterns-wip/coffeeshop/helm

#### Create the project Secret
Export the following environment variables

```
DB_APP_NAME=coffeeshopdb
DB_SECRET_NAME=$DB_APP_NAME-secret
DB_NAME=coffeeshop
DB_USER=delta
DB_PWD=delta123
```

Create a secret containing your Database info (skip this step if already created):
```
oc create secret generic $DB_SECRET_NAME -n $OPENSHIFT_NAMESPACE \
--from-literal=database-name=$DB_NAME \
--from-literal=database-user=$DB_USER \
--from-literal=database-password=$DB_PWD
```

#### Create the application Deployment and Deployment Tekton Pipeline
Clone version 1.3.7 of `openshift-tekton` repository:
```
git clone -b v1.3.7 https://git.delta.com/ccoe/openshift-tekton.git
```

Run the following command to create a Tekton Deployment pipeline template:
```
oc apply -f openshift-tekton/Java -f openshift-tekton/CommonTasks -f openshift-tekton/IaC-Deploy -n $OPENSHIFT_NAMESPACE
```

Create tekton pipeline:
```
oc process java-create-app -p APP_NAME=counter \
-p BUILD_TYPE=quarkus-fast-jar -p GIT_BRANCH=main \
-p GIT_REPOSITORY=https://git.delta.com/ccoe/implementation-patterns-wip/coffeeshop/counter.git \
| oc apply -n $OPENSHIFT_NAMESPACE -f -
```
NOTE: it may take 1-2 minutes for the pipeline to get created

### Execute the pipeline
Execute pipeline via OpenShift Console:
1. Select **Home > Projects** from the left-hand menu, and click on your project name.
1. Select **Pipelines > Pipelines** from the left-hand menu.
1. Click on the hamburger menu (the three dots) to the right of the `counter-pipeline`, and click **Start**.
1. On the **Start Pipeline** screen, scroll down to **Workspaces** and select **PVC** > **counter-pipeline-workspace**.
1. Click **Start**

