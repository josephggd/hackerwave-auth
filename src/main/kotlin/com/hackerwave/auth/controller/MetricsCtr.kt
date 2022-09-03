package com.hackerwave.auth.controller

import com.hackerwave.auth.messaging.service.ConsumerSvc
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Metrics API", description = "Get Kafka messages and aggregations on user activity")
@RestController("api/\${custom.api.prefix.version}/\${custom.api.prefix.messaging}/")
class MetricsCtr(
    private val consumerSvc: ConsumerSvc
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("gp")
    fun getAggregatedMetricsBy(
        @RequestParam gpType: CommonStrings.Grouping
    ): ResponseEntity<Any> {
        val functionDescr = "getMetricsByAction"
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, functionDescr)
        val loginHistoryDto = consumerSvc.groupBy(gpType)
        return ResponseEntity
            .ok()
            .body(loginHistoryDto)
//        try {
//            val loginHistoryDto = consumerSvc.groupByAction(gpType)
//            return ResponseEntity
//                .ok()
//                .body(loginHistoryDto)
//        } catch (expectedException: UserNotFoundException){
//            logger.warn(loggerMsg, CommonStrings.FunctionState.FAILURE, functionDescr)
//            return ResponseEntity
//                .badRequest()
//                .build()
//        } catch (anyOtherException: Exception){
//            logger.warn(loggerMsg, CommonStrings.FunctionState.FAILURE, functionDescr)
//            return ResponseEntity
//                .badRequest()
//                .build()
//        }
    }
}