package com.procurement.docs_generator.infrastructure.adapter

import com.procurement.docs_generator.adapter.KafkaProcessor
import com.procurement.docs_generator.application.service.kafka.KafkaMessageHandler
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.logger.debug
import com.procurement.docs_generator.domain.logger.error
import com.procurement.docs_generator.domain.logger.info
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import org.apache.kafka.clients.consumer.CommitFailedException
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.AuthenticationException
import org.apache.kafka.common.errors.AuthorizationException
import org.apache.kafka.common.errors.InterruptException
import org.springframework.stereotype.Service
import java.time.Duration
import javax.annotation.PostConstruct

@Service
class KafkaProcessorImpl(
    private val consumerFactory: KafkaConsumerFactory<String, String>,
    private val producer: KafkaProducer<String, String>,
    private val messageHandler: KafkaMessageHandler) : KafkaProcessor {

    companion object {
        private val log: Logger = Slf4jLogger()

        private const val TopicIn = "document-generator-in"
        private const val TopicOut = "document-generator-out"

        private val InitialDelay = Duration.ofMillis(100)
        private val MaxDelay = Duration.ofSeconds(60)
        private val PollTimeout = Duration.ofMillis(100)
    }

    @PostConstruct
    override fun processing() {
        log.info { "Run message processing..." }
        GlobalScope.launch {
            while (true) {
                val consumer = consumerFactory.create(listOf(TopicIn))

                poolLoop@
                while (true) {
                    val records: ConsumerRecords<String, String> = consumer.poll(PollTimeout)
                    if (!records.isEmpty) {
                        log.debug { "Fetch ${records.count()} records." }

                        for (record in records) {
                            val message = handler(record)
                            reply(message)
                        }

                        if (!consumer.commit()) break@poolLoop
                    }
                }
            }
        }
    }

    private fun handler(record: ConsumerRecord<String, String>): String {
        log.debug { "Handle record with key: '${record.key()}' and value: '${record.value()}'" }
        val result = messageHandler.handle(record.value())
        log.debug { "Handled record with key: '${record.key()}' and value: '${record.value()}'. Result: '$result'." }
        return result
    }

    private suspend fun reply(message: String) {
        log.debug { "Attempt sending a message to Kafka topic: '$TopicOut'." }
        val record = ProducerRecord<String, String>(TopicOut, message)
        retryReply {
            producer.send(record)
        }
        log.debug { "A message was sent to Kafka topic: '$TopicOut'." }
    }

    private suspend fun retryReply(block: suspend () -> Unit) {
        var delayAmount: Duration = InitialDelay
        while (true) {
            try {
                return block()
            } catch (exception: Exception) {
                log.error(exception) {
                    "Error of sending a message to Kafka topic '$TopicOut'." +
                        if (exception.message != null)
                            ". ${exception.message}"
                        else
                            ""
                }
            }

            delay(delayAmount)
            delayAmount = delayAmount.multipliedBy(2)
            if(delayAmount > MaxDelay)
                delayAmount = MaxDelay
        }
    }

    private suspend fun KafkaConsumer<String, String>.commit(): Boolean {
        var delayAmount: Duration = InitialDelay
        while (true) {
            try {
                log.debug { "Attempt to commit the records from Kafka." }
                this.commitSync()
                log.debug { "Commit is success." }
                return true
            } catch (exception: Exception) {
                log.error(exception) {
                    "Error of commit a message to Kafka topic '$TopicOut'." +
                        if (exception.message != null) " ${exception.message}" else ""
                }

                when (exception) {
                    is CommitFailedException,
                    is InterruptException,
                    is AuthenticationException,
                    is AuthorizationException,
                    is IllegalStateException -> {
                        this.close()
                        return false
                    }
                }
            }

            delay(delayAmount)
            delayAmount = delayAmount.multipliedBy(2)
            if(delayAmount > MaxDelay) delayAmount = MaxDelay
        }
    }
}