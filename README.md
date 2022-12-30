# About this Tech hub

This tech hub will help to establish *AWS connection with localstack*. For an instance, here is an implementaions of AWS SQS service and its connection with localstack.

## Localstack

LocalStack is an open-source mock of the real AWS services. It provides a testing environment on our local machine with the same APIs as the real AWS services. We switch to using the real AWS services only in the integration environment and beyond.

## Why use Localstack

The method of temporarily using dummy (or mock, fake, proxy) objects in place of actual ones is a popular way of running tests for applications with external dependencies. Most appropriately, these dummies are called Test Doubles. We can implement test doubles of our AWS services with LocalStack.
LocalStack supports:
- running our applications without connecting to AWS.
- avoiding the complexity of AWS configuration and focus on development.
- running tests in our CI/CD pipeline.
- configuring and testing error scenarios.

## Running LocalStack as a Docker container

LocalStack can be started within a single docker container. It has quite some possibilities to change it’s configuration. By setting the right environment variables you can configure what service you want to enable. The single container will be the host of the LocalStack application, to reach certain services you need to address a single edge service of LocalStack that is exposed on port 4566.
Before running LocalStack as a Docker container make sure you have installed AWS CLI, docker and docker-compose in your system.
We can run LocalStack directly as a Docker image either by using the docker run command or with docker-compose. We will use docker-compose. For that, we will use the base version of docker-compose.yml file and customize it as per our requirements. You can also run it without changes if you prefer to use the default configuration.

## Contact

- Work email: pragati.dubey@knoldus.com
- LinkedIn: https://www.linkedin.com/in/pragati-dubey-368624197/
- My tech articles: https://blog.knoldus.com/author/pragatiatknoldus/

<img src="https://futurumresearch.com/wp-content/uploads/2020/01/aws-logo.png" style="height:500px">

