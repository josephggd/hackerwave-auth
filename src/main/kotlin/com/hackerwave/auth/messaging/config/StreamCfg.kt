package com.hackerwave.auth.messaging.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.state.HostInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class StreamCfg {
    @Value("\$kafka.streams.host.info")
    lateinit var hostStr: String

    @Value("\${server.port}")
    lateinit var port: String

    @Value("\${spring.kafka.template.default-topic}")
    lateinit var defaultTopic: String

    @Value("\${spring.kafka.streams.application-id}")
    lateinit var applicationId: String

    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var servers: String
}