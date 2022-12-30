package com.knoldus.aws.models

case class AWSConfig(accessKey: String, secretKey: String, region: String)

case class SQSEndpoint(serviceEndpoint: String)

case class SQSConfig(awsConfig: AWSConfig, sqsConfig: SQSEndpoint)
