package com.knoldus.aws.services

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs._
import com.amazonaws.services.sqs.model._
import com.knoldus.aws.aws.{AWSService, CredentialsLookup}
import com.knoldus.aws.models.QueueType.QueueType
import com.knoldus.aws.models.{Queue, QueueType, SQSConfig, SQSEndpoint}
import com.typesafe.scalalogging.LazyLogging
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.CollectionConverters.{CollectionHasAsScala, MapHasAsJava}

trait SQSService extends AWSService with LazyLogging {

  private val sqsConfig: SQSEndpoint = ConfigSource.resources("application.conf").load[SQSEndpoint] match {
    case Left(e: ConfigReaderFailures) =>
      throw new RuntimeException(s"Unable to load AWS Config, original error: ${e.prettyPrint()}")
    case Right(x) => x
  }

  val config: SQSConfig = SQSConfig(awsConfig, sqsConfig)
  val amazonSQSClient: AmazonSQS

  def createQueue(queueName: String, queueType: QueueType = QueueType.STANDARD): Either[Throwable, Queue] =
    try {
      val createQueueRequest = createQueueRequestFromType(queueName, queueType)
      if (!listQueues.map(_.queueName).contains(createQueueRequest.getQueueName))
        Right(Queue(amazonSQSClient.createQueue(createQueueRequest).getQueueUrl))
      else
        throw new QueueNameExistsException("Queue with the same name already exists.")
    } catch {
      case t: Throwable => Left(new Throwable(s"Exception: $t"))
    }

  private def createQueueRequestFromType(name: String, queueType: QueueType): CreateQueueRequest =
    if (queueType.equals(QueueType.FIFO)) {
      val queueAttributes = Map(
        QueueAttributeName.FifoQueue.toString -> "true",
        QueueAttributeName.ContentBasedDeduplication.toString -> "true"
      ).asJava
      new CreateQueueRequest(name + ".fifo").withAttributes(queueAttributes)
    } else
      new CreateQueueRequest(name)

  def listQueues: Seq[Queue] =
    amazonSQSClient.listQueues().getQueueUrls.asScala.map(urls => Queue(urls)).toSeq

  def deleteQueue(queue: Queue): Future[DeleteQueueResult] =
    Future.successful(amazonSQSClient.deleteQueue(queue.url))

  def deleteAllQueues(queues: Seq[Queue]): Future[Seq[DeleteQueueResult]] =
    Future.sequence(queues.map { queue =>
      Future.successful(amazonSQSClient.deleteQueue(queue.url))
    })
}

object SQSService extends SQSService {

  override val amazonSQSClient: AmazonSQS = {
    val credentials: AWSCredentialsProvider =
      CredentialsLookup.getCredentialsProvider(awsConfig.accessKey, awsConfig.secretKey)
    buildAmazonSQSClient(config, credentials)
  }

  def buildAmazonSQSClient(configuration: SQSConfig, credentials: AWSCredentialsProvider): AmazonSQS = {
    val sqsEndpointConfiguration: AwsClientBuilder.EndpointConfiguration =
      new AwsClientBuilder.EndpointConfiguration(
        configuration.sqsConfig.serviceEndpoint,
        configuration.awsConfig.region
      )

    val clientBuilder = AmazonSQSClientBuilder.standard
    clientBuilder.setCredentials(credentials)
    clientBuilder.setEndpointConfiguration(sqsEndpointConfiguration)
    clientBuilder.build
  }
}
