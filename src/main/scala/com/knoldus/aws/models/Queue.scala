package com.knoldus.aws.models

import com.amazonaws.services.sqs.model._
import com.knoldus.aws.models
import com.knoldus.aws.models.QueueType.QueueType
import com.knoldus.aws.services.SQSService

import scala.concurrent.Future

case class Queue(url: String) {

  val queueName: String = this.url.split("/").last

  val queueType: QueueType = if (queueName.contains(".fifo")) QueueType.FIFO else QueueType.STANDARD

  def destroy()(implicit sqs: SQSService): Future[DeleteQueueResult] = sqs.deleteQueue(this)

}

object QueueType extends Enumeration {
  type QueueType = Value

  val STANDARD: models.QueueType.Value = Value("STANDARD")
  val FIFO: models.QueueType.Value = Value("FIFO")
}
