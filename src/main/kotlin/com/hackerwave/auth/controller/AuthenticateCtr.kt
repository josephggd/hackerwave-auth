package com.hackerwave.auth.controller

import com.hackerwave.auth.dto.HwUserDto
import com.hackerwave.auth.entity.HwUser
import com.hackerwave.auth.service.AuthenticateSvc
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.Companion.loggerMsg
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("\${api.prefix.authentication}")
class AuthenticateCtr(
    private val authenticateSvc: AuthenticateSvc
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("user")
    fun authenticateUser(
        // TODO: Add security to parse headers f/initial login
    ):ResponseEntity<HwUserDto>{
        val functionDescr = "authenticateUser"
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, functionDescr)
        try {
            val hwUser:HwUser = authenticateSvc.logInUser(
                email,
                gId
            )
            return ResponseEntity.ok(hwUser.toDto())
        } catch (expectedException: IllegalArgumentException){
            logger.warn(loggerMsg, CommonStrings.FunctionState.FAILURE, functionDescr)
            return ResponseEntity.badRequest().build()
        } catch (anyOtherException: Exception){
            logger.warn(loggerMsg, CommonStrings.FunctionState.FAILURE, functionDescr)
            return ResponseEntity.badRequest().build()
        }
    }

}