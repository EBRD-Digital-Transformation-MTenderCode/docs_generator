package com.procurement.docs_generator.configuration.properties

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration
import java.util.*
import kotlin.collections.HashMap

@ConfigurationProperties(prefix = "kafka")
class KafkaProperties {

    /**
     * Comma-delimited list of host:port pairs to use for establishing the initial
     * connection to the Kafka cluster.
     */
    var bootstrapServers: List<String> = listOf("localhost:8888")

    /**
     * ID to pass to the server when making requests. Used for server-side logging.
     */
    var clientId: String? = null

    /**
     * Additional properties, common to producers and consumers, used to configure the
     * client.
     */
    var properties: Map<String, String> = HashMap()

    var consumer = Consumer()

    var producer = Producer()

    var admin = Admin()

    var ssl = Ssl()

    var jaas = Jaas()

    var template = Template()

    private fun buildCommonProperties(): Properties {
        return Properties().apply {
            this[CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
            clientId?.also { this[CommonClientConfigs.CLIENT_ID_CONFIG] = it }
            putAll(ssl.buildProperties())
            if (properties.isNotEmpty()) putAll(properties)
        }
    }

    /**
     * Create an initial map of consumer properties from the state of this instance.
     *
     *
     * This allows you to add additional properties, if necessary, and override the
     * default.
     * @return the consumer properties initialized with the customizations defined on this
     * instance
     */
    fun buildConsumerProperties(): Properties {
        return buildCommonProperties().apply {
            putAll(consumer.buildProperties())
        }
    }

    /**
     * Create an initial map of producer properties from the state of this instance.
     *
     *
     * This allows you to add additional properties, if necessary, and override the
     * default.
     * @return the producer properties initialized with the customizations defined on this
     * instance
     */
    fun buildProducerProperties(): Properties {
        return buildCommonProperties().apply {
            putAll(producer.buildProperties())
        }
    }

    /**
     * Create an initial map of admin properties from the state of this instance.
     *
     *
     * This allows you to add additional properties, if necessary, and override the
     * default.
     * @return the admin properties initialized with the customizations defined on this
     * instance
     */
    fun buildAdminProperties(): Properties {
        return buildCommonProperties().apply {
            putAll(admin.buildProperties())
        }
    }

    class Consumer {

        var ssl = Ssl()

        /**
         * Frequency with which the consumer offsets are auto-committed to Kafka if
         * 'enable.auto.commit' is set to true.
         */
        var autoCommitInterval: Duration? = null

        /**
         * What to do when there is no initial offset in Kafka or if the current offset no
         * longer exists on the server.
         */
        var autoOffsetReset: String? = null

        /**
         * Comma-delimited list of host:port pairs to use for establishing the initial
         * connection to the Kafka cluster.
         */
        var bootstrapServers: List<String>? = null

        /**
         * ID to pass to the server when making requests. Used for server-side logging.
         */
        var clientId: String? = null

        /**
         * Whether the consumer's offset is periodically committed in the background.
         */
        var enableAutoCommit: Boolean? = null

        /**
         * Maximum amount of time the server blocks before answering the fetch request if
         * there isn't sufficient data to immediately satisfy the requirement given by
         * "fetch.min.bytes".
         */
        var fetchMaxWait: Duration? = null

        /**
         * Minimum amount of data, in bytes, the server should return for a fetch request.
         */
        var fetchMinSize: Int? = null

        /**
         * Unique string that identifies the consumer group to which this consumer
         * belongs.
         */
        var groupId: String? = null

        /**
         * Expected time between heartbeats to the consumer coordinator.
         */
        var heartbeatInterval: Duration? = null

        /**
         * Deserializer class for keys.
         */
        var keyDeserializer: Class<*> = StringDeserializer::class.java

        /**
         * Deserializer class for values.
         */
        var valueDeserializer: Class<*> = StringDeserializer::class.java

        /**
         * Maximum number of records returned in a single call to poll().
         */
        var maxPollRecords: Int? = null

        /**
         * Additional consumer-specific properties used to configure the client.
         */
        var properties: Map<String, String> = HashMap()

        fun buildProperties(): Properties {
            return Properties().apply {
                autoCommitInterval?.also { this[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = it }
                autoOffsetReset?.also { this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = it }
                bootstrapServers?.also { this[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = it }
                clientId?.also { this[ConsumerConfig.CLIENT_ID_CONFIG] = it }
                enableAutoCommit?.also { this[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = it }
                fetchMaxWait?.also { this[ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG] = it }
                fetchMinSize?.also { this[ConsumerConfig.FETCH_MIN_BYTES_CONFIG] = it }
                groupId?.also { this[ConsumerConfig.GROUP_ID_CONFIG] = it }
                heartbeatInterval?.also { this[ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG] = it }
                this[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = keyDeserializer
                this[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = valueDeserializer
                maxPollRecords?.also { this[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = it }
                putAll(ssl.buildProperties())
                if (properties.isNotEmpty()) putAll(properties)
            }
        }
    }

    class Producer {

        var ssl = Ssl()

        /**
         * Number of acknowledgments the producer requires the leader to have received
         * before considering a request complete.
         */
        var acks: String? = null

        /**
         * Number of records to batch before sending.
         */
        var batchSize: Int? = null

        /**
         * Comma-delimited list of host:port pairs to use for establishing the initial
         * connection to the Kafka cluster.
         */
        var bootstrapServers: List<String>? = null

        /**
         * Total bytes of memory the producer can use to buffer records waiting to be sent
         * to the server.
         */
        var bufferMemory: Long? = null

        /**
         * ID to pass to the server when making requests. Used for server-side logging.
         */
        var clientId: String? = null

        /**
         * Compression type for all data generated by the producer.
         */
        var compressionType: String? = null

        /**
         * Serializer class for keys.
         */
        var keySerializer: Class<*> = StringSerializer::class.java

        /**
         * Serializer class for values.
         */
        var valueSerializer: Class<*> = StringSerializer::class.java

        /**
         * When greater than zero, enables retrying of failed sends.
         */
        var retries: Int? = null

        /**
         * Additional producer-specific properties used to configure the client.
         */
        var properties: Map<String, String> = HashMap()

        fun buildProperties(): Properties {
            return Properties().apply {
                acks?.also { this[ProducerConfig.ACKS_CONFIG] = it }
                batchSize?.also { this[ProducerConfig.BATCH_SIZE_CONFIG] = it }
                bootstrapServers?.also { this[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = it }
                bufferMemory?.also { this[ProducerConfig.BUFFER_MEMORY_CONFIG] = it }
                clientId?.also { this[ProducerConfig.CLIENT_ID_CONFIG] = it }
                compressionType?.also { this[ProducerConfig.COMPRESSION_TYPE_CONFIG] = it }
                this[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = keySerializer
                this[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = valueSerializer
                retries?.also { this[ProducerConfig.RETRIES_CONFIG] = it }
                putAll(ssl.buildProperties())
                if (properties.isNotEmpty()) putAll(properties)
            }
        }
    }

    class Admin {

        var ssl = Ssl()

        /**
         * ID to pass to the server when making requests. Used for server-side logging.
         */
        var clientId: String? = null

        /**
         * Additional admin-specific properties used to configure the client.
         */
        val properties: Map<String, String> = HashMap()

        /**
         * Whether to fail fast if the broker is not available on startup.
         */
        var isFailFast: Boolean = false

        fun buildProperties(): Properties {
            return Properties().apply {
                clientId?.also { this[ProducerConfig.CLIENT_ID_CONFIG] = it }
                putAll(ssl.buildProperties())
                if (properties.isNotEmpty()) putAll(properties)
            }
        }
    }

    class Template {

        /**
         * Default topic to which messages are sent.
         */
        var defaultTopic: String? = null
    }

    class Ssl {

        /**
         * Password of the private key in the key store file.
         */
        var keyPassword: String? = null

        /**
         * Location of the key store file.
         */
        var keystoreLocation: String? = null

        /**
         * Store password for the key store file.
         */
        var keystorePassword: String? = null

        /**
         * Type of the key store.
         */
        var keyStoreType: String? = null

        /**
         * Location of the trust store file.
         */
        var truststoreLocation: String? = null

        /**
         * Store password for the trust store file.
         */
        var truststorePassword: String? = null

        /**
         * Type of the trust store.
         */
        var trustStoreType: String? = null

        /**
         * SSL protocol to use.
         */
        var protocol: String? = null

        fun buildProperties(): Properties {
            return Properties().apply {
                keyPassword?.also { this[SslConfigs.SSL_KEY_PASSWORD_CONFIG] = it }
                keystoreLocation?.also { this[SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG] = it }
                keystorePassword?.also { this[SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG] = it }
                keyStoreType?.also { this[SslConfigs.SSL_KEYSTORE_TYPE_CONFIG] = it }
                truststoreLocation?.also { this[SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG] = it }
                truststorePassword?.also { this[SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG] = it }
                trustStoreType?.also { this[SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG] = it }
                protocol?.also { this[SslConfigs.SSL_PROTOCOL_CONFIG] = it }
            }
        }
    }

    class Jaas {

        /**
         * Whether to enable JAAS configuration.
         */
        var isEnabled: Boolean = false

        /**
         * Login module.
         */
        var loginModule = "com.sun.security.auth.module.Krb5LoginModule"

        /**
         * Control flag for login configuration.
         */
        var controlFlag = ControlFlag.REQUIRED

        /**
         * Additional JAAS options.
         */
        var options: Map<String, String> = HashMap()

        enum class ControlFlag {

            /**
             * Required - The `LoginModule` is required to succeed. If it succeeds or
             * fails, authentication still continues to proceed down the `LoginModule`
             * list.
             *
             */
            REQUIRED,

            /**
             * Requisite - The `LoginModule` is required to succeed. If it succeeds,
             * authentication continues down the `LoginModule` list. If it fails,
             * control immediately returns to the application (authentication does not proceed
             * down the `LoginModule` list).
             */
            REQUISITE,

            /**
             * Sufficient - The `LoginModule` is not required to succeed. If it does
             * succeed, control immediately returns to the application (authentication does
             * not proceed down the `LoginModule` list). If it fails, authentication
             * continues down the `LoginModule` list.
             */
            SUFFICIENT,

            /**
             * Optional - The `LoginModule` is not required to succeed. If it succeeds
             * or fails, authentication still continues to proceed down the
             * `LoginModule` list.
             */
            OPTIONAL
        }
    }
}