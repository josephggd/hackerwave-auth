package com.hackerwave.auth.messaging.config

import com.hackerwave.auth.dto.LoginDto
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class ProducerCfg {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Value("\${spring.kafka.bootstrap-servers}")
    private val servers: String? = null

    @Value("\${spring.kafka.properties.sasl.jaas.config}")
    private val jaasConfig: String? = null

    @Value("\${spring.kafka.consumer.group-id}")
    private val groupId: String? = null
    @Bean
    @ConditionalOnProperty(name = ["cloudkarafka.enabled"], havingValue = "true")
    fun cloudProducerFactory(): ProducerFactory<String, LoginDto> {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "cloudProducerFactory")
        val props: MutableMap<String, Any?> = HashMap()
        props["group.id"] = groupId
        props["sasl.jaas.config"] = jaasConfig
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        props["enable.auto.commit"] = "true"
        props["auto.commit.interval.ms"] = "1000"
        props["auto.offset.reset"] = "earliest"
        props["session.timeout.ms"] = "30000"
        props["security.protocol"] = "SASL_SSL"
        props["sasl.mechanism"] = "SCRAM-SHA-256"
        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    @ConditionalOnProperty(name = ["cloudkarafka.enabled"], havingValue = "false")
    fun devProducerFactory(): ProducerFactory<String, LoginDto> {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "devProducerFactory")
        val props: MutableMap<String, Any?> = HashMap()
        props["group.id"] = groupId
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun kafkaTemplate(kafkaProducerFactory: ProducerFactory<String, LoginDto>): KafkaTemplate<String, LoginDto> {
        val kafkaTemplate: KafkaTemplate<String, LoginDto> = KafkaTemplate(kafkaProducerFactory)
        kafkaProducerFactory.createProducer()
        return kafkaTemplate
    }
}