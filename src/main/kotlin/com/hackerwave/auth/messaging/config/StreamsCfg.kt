package com.hackerwave.auth.messaging.config

import com.hackerwave.auth.messaging.StreamTopology
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.state.HostInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class StreamsCfg {
    @Value("\${kafka.streams.state.dir:/tmp/kafka-streams/loginHistory}")
    private lateinit var stateDir: String

    @Value("\$kafka.streams.host.info")
    private lateinit var hostInfo: String

    @Value("\${server.port}")
    private lateinit var port: String

    @Value("\${spring.kafka.template.default-topic}")
    private lateinit var defaultTopic: String

    @Value("\${spring.kafka.streams.application-id}")
    private lateinit var applicationId: String

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var servers: String

    @Bean
    fun kafkaStreams(): KafkaStreams {
        val properties = Properties()
        properties[StreamsConfig.APPLICATION_ID_CONFIG] = applicationId
        properties[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        properties[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
        properties[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
        properties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        properties[StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG] = "0"
        properties[StreamsConfig.APPLICATION_SERVER_CONFIG] = "${hostInfo}:${port}"
        properties[StreamsConfig.STATE_DIR_CONFIG] = stateDir
        val topology: Topology = StreamTopology.buildTopology(defaultTopic)
        val kafkaStreams = KafkaStreams(topology, properties)
        kafkaStreams.cleanUp()
        kafkaStreams.start()
        Runtime.getRuntime().addShutdownHook(Thread { kafkaStreams.close() })
        return kafkaStreams
    }

    @Bean
    fun hostInfo(): HostInfo {
        return HostInfo(hostInfo, port.toInt())
    }
}