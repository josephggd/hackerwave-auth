package com.hackerwave.auth.messaging.config

import com.hackerwave.auth.util.CommonStrings.actionTopic
import com.hackerwave.auth.util.CommonStrings.dateTopic
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class TopicCfg {
    @Value("\${spring.kafka.template.default-topic}")
    private lateinit var defaultTopic: String
    @Bean
    fun buildLoginTopic(): NewTopic {
        return TopicBuilder.name(defaultTopic)
            .build()
    }

    @Bean
    fun buildActionAggTopic(): NewTopic {
        return TopicBuilder.name(actionTopic)
            .build()
    }

//    @Bean
//    fun buildDateAggTopic(): NewTopic {
//        return TopicBuilder.name(dateTopic)
//            .build()
//    }
}