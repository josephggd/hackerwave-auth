package com.hackerwave.auth.messaging.service

import com.hackerwave.auth.dto.HistoryDto
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import com.hackerwave.auth.util.CommonStrings.historyStore
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StoreQueryParameters
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class ConsumerSvc @Autowired constructor(private val kafkaStreams: KafkaStreams) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    fun groupByAction(id: UUID): HistoryDto? {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "findOrderHistory")
        return store[id.toString()]
    }

    val store: ReadOnlyKeyValueStore<String, HistoryDto>
        get() {
            logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "getStore")
            return kafkaStreams
                .store<ReadOnlyKeyValueStore<String, HistoryDto>>(
                    StoreQueryParameters.fromNameAndType(
                        historyStore,
                        QueryableStoreTypes.keyValueStore()
                    )
                )
        }
}