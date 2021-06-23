# coffesshop counter service

This project implements an event driven counter microservice for the Quarkus coffeeshop project

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

Install infrastructure components by following instructions from: https://git.delta.com/ccoe/implementation-patterns-wip/coffeeshop/helm

Export the following environment variables

```
OPENSHIFT_NAMESPACE=quarkuscoffee
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
-p BUILD_TYPE=quarkus-fast-jar -p GIT_BRANCH=master \
-p GIT_REPOSITORY=https://git.delta.com/ccoe/implementation-patterns-wip/coffeeshop/counter.git \
| oc apply -n $OPENSHIFT_NAMESPACE -f -
```
NOTE: it may take 1-2 minutes for the pipeline to get created

Execute pipeline via OpenShift Console:
1. Select **Home > Projects** from the left-hand menu, and click on your project name.
1. Select **Pipelines > Pipelines** from the left-hand menu.
1. Click on the hamburger menu (the three dots) to the left of the `coffeeshop-web-pipeline`, and click **Start**.
1. On the **Start Pipeline** screen, scroll down to **Workspaces** and select **PVC** > **coffeeshop-web-pipeline-workspace**.
1. Click **Start**

