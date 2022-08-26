package com.hackerwave.auth.util

import com.hackerwave.auth.dto.ActionDto
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MsgFunctions {
    private val logger: Logger = LoggerFactory.getLogger(MsgFunctions::class.java)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss")

    fun generateAction(action: CommonStrings.UserAction):ActionDto{
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "generateAction")
        val now = LocalDateTime.now()
        val date = now.format(dateFormatter)
        val time = now.format(timeFormatter)
        return ActionDto(date, time, action)
    }

    fun determineGrouping( grouping:String, actionDto: ActionDto ):String{
        when(grouping){
            CommonStrings.Grouping.DATE.toString() -> return actionDto.date
            CommonStrings.Grouping.ACTION.toString() -> return actionDto.action.toString()
        }
        throw IllegalArgumentException("NO ACTION")
    }
}