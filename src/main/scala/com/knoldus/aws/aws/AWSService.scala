package com.knoldus.aws.aws

import com.knoldus.aws.models.AWSConfig
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._

trait AWSService {

  val awsConfig: AWSConfig = ConfigSource.resources("application.conf").load[AWSConfig] match {
    case Left(e: ConfigReaderFailures) =>
      throw new RuntimeException(s"Unable to load AWS Config, original error: ${e.prettyPrint()}")
    case Right(awsConfig) => awsConfig
  }
}
