package com.knoldus.aws.services

import cloud.localstack.TestUtils
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.DeleteQueueResult
import com.knoldus.aws.models.{Queue, QueueType}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class SQSSpec extends AnyFlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll {
  behavior of "SQS"

  val sqsService: SQSService = new SQSService {
    override val amazonSQSClient: AmazonSQS = TestUtils.getClientSQS("http://localhost:4566/")
  }

  val standardQueue: Queue = sqsService.listQueues
    .find(_.queueName.equals("testStandardQueue"))
    .getOrElse(Queue("http://localhost:4566/000000000000/testStandardQueue"))

  val fifoQueue: Queue = sqsService.listQueues
    .find(_.queueName.equals("testFifoQueue"))
    .getOrElse(Queue("http://localhost:4566/000000000000/testFifoQueue.fifo"))

  val expectedQueue: Queue = Queue("http://localhost:4566/000000000000/dummyQueue")
  val expectedFifoQueue: Queue = Queue("http://localhost:4566/000000000000/dummyQueue.fifo")

  override def beforeAll(): Unit = {
    sqsService.createQueue("testStandardQueue")
    sqsService.createQueue("testFifoQueue", QueueType.FIFO)
  }

  override def afterAll: Unit = {
    val queues = sqsService.listQueues
    sqsService.deleteAllQueues(queues)
  }

  it should "create new standard queue" in {
    val queueResult = sqsService.createQueue("dummyQueue").toOption
    assert(queueResult.contains(expectedQueue))
    sqsService.deleteQueue(expectedQueue)
  }

  it should "create new FIFO queue" in {
    val queueResult = sqsService.createQueue("dummyQueue", QueueType.FIFO).toOption
    assert(queueResult.contains(expectedFifoQueue))
    sqsService.deleteQueue(expectedFifoQueue)
  }

  it should "list all queues" in {
    val queueResultSeq = sqsService.listQueues
    assert(queueResultSeq == Seq(standardQueue, fifoQueue))
  }

  it should "delete a standard queue" in {
    val queue = sqsService.createQueue("dummyQueue").toOption.getOrElse(expectedQueue)
    val deleteQueueResult = sqsService.deleteQueue(queue)
    whenReady(deleteQueueResult, timeout(5 seconds)) { result =>
      assert(result.isInstanceOf[DeleteQueueResult])
    }
  }

  it should "delete a FIFO queue" in {
    val queue = sqsService.createQueue("dummyQueue").toOption.getOrElse(expectedFifoQueue)
    val deleteQueueResult = sqsService.deleteQueue(queue)
    whenReady(deleteQueueResult, timeout(5 seconds)) { result =>
      assert(result.isInstanceOf[DeleteQueueResult])
    }
  }
}
