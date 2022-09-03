package com.hackerwave.auth.messaging.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

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