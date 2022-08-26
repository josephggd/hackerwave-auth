package com.hackerwave.auth.messaging.service

import com.hackerwave.auth.dto.ActionAggDto
import com.hackerwave.auth.dto.DateAggDto
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StoreQueryParameters
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ConsumerSvc @Autowired constructor(
    private val dateStream: KafkaStreams,
    private val actionStream: KafkaStreams
    ) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun groupBy(gpType:CommonStrings.Grouping): Any? {
        println("gpType:"+gpType)
        println("gpType:"+gpType.toString()===CommonStrings.Grouping.ACTION.toString())
        return groupByAction(gpType)
    }

    fun groupByAction(actionType:CommonStrings.Grouping): ActionAggDto? {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "groupByAction")
        return actionStore[actionType.toString()]
    }
    fun groupByDate(actionType:CommonStrings.Grouping): DateAggDto? {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "groupByDate")
        return dateStore[actionType.toString()]
    }

    val actionStore: ReadOnlyKeyValueStore<String, ActionAggDto>
        get() {
            logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "getStore")
            return actionStream
                .store<ReadOnlyKeyValueStore<String, ActionAggDto>>(
                    StoreQueryParameters.fromNameAndType(
                        CommonStrings.actionStore,
                        QueryableStoreTypes.keyValueStore()
                    )
                )
        }

    val dateStore: ReadOnlyKeyValueStore<String, DateAggDto>
        get() {
            logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "getStore")
            return dateStream
                .store<ReadOnlyKeyValueStore<String, DateAggDto>>(
                    StoreQueryParameters.fromNameAndType(
                        CommonStrings.dateStore,
                        QueryableStoreTypes.keyValueStore()
                    )
                )
        }
}