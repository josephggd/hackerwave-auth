package com.hackerwave.auth.messaging.config

import com.hackerwave.auth.messaging.DateAggTopology
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.gpByAllStore
import com.hackerwave.auth.util.CommonStrings.gpByAllTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.state.HostInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class DateStreamCfg : StreamCfg() {
    @Value("\${kafka.streams.state.dir:/tmp/kafka-streams/dateAgg}")
    lateinit var stateDir: String

    @Bean
    fun dateHostInfo(): HostInfo {
        return HostInfo(hostStr, port.toInt())
    }

    @Bean
    fun dateProperties(): Properties {
        val properties = Properties()
        properties[StreamsConfig.APPLICATION_ID_CONFIG] = applicationId
        properties[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        properties[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
        properties[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
        properties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        properties[StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG] = "0"
        properties[StreamsConfig.APPLICATION_SERVER_CONFIG] = "${hostStr}:${port}"
        properties[StreamsConfig.STATE_DIR_CONFIG] = stateDir
        properties[StreamsConfig.STATE_DIR_CONFIG] = stateDir
        return properties
    }

    @Bean
    @Autowired
    fun dateStream(@Qualifier("dateProperties") dateProperties:Properties): KafkaStreams {
        val topology: Topology = DateAggTopology.buildTopology(defaultTopic)
        val kafkaStreams = KafkaStreams(topology, dateProperties)
        kafkaStreams.cleanUp()
        kafkaStreams.start()
        Runtime.getRuntime().addShutdownHook(Thread { kafkaStreams.close() })
        return kafkaStreams
    }
}