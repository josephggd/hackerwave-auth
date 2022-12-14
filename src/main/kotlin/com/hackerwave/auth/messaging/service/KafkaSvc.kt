package com.hackerwave.auth.messaging.service

import com.hackerwave.auth.dto.ActionDto
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.dateFormatter
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import com.hackerwave.auth.util.CommonStrings.timeFormatter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class KafkaSvc(
    template: KafkaTemplate<String, ActionDto>
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Value("\${spring.kafka.template.default-topic}")
    private val defaultTopic: String? = null
    private val template: KafkaTemplate<String, ActionDto>

    init {
        this.template = template
    }

    fun submitAction(
        action: CommonStrings.UserAction
    ) {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "submitAction")
        val now = LocalDateTime.now()
        val date = now.format(dateFormatter)
        val time = now.format(timeFormatter)
        val actionDto = ActionDto(date, time, action)
        template.send(defaultTopic!!, actionDto)
    }
}