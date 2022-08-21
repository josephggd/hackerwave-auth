package com.hackerwave.auth.messaging.service

import com.hackerwave.auth.dto.LoginDto
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaSvc(
    template: KafkaTemplate<String, LoginDto>
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Value("\${spring.kafka.template.default-topic}")
    private val defaultTopic: String? = null
    private val template: KafkaTemplate<String, LoginDto>

    init {
        this.template = template
    }

    fun submitAction(
        loginDto: LoginDto
    ) {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "submitAction")
        template.send(defaultTopic!!, loginDto.id.toString(), loginDto)
    }
}