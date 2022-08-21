package com.hackerwave.auth.controller

import com.hackerwave.auth.entity.HwUser
import com.hackerwave.auth.service.AuthSvc
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.authorizationHeader
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import com.hackerwave.auth.util.JwtFunctions.encodeToJwt
import com.hackerwave.auth.util.exception.UserExistsException
import com.hackerwave.auth.util.exception.UserNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController("\${custom.api.prefix.authentication}")
class AuthenticateCtr(
    private val authSvc: AuthSvc
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("un")
    fun authenticateUser(
        @RequestHeader(name=authorizationHeader) authHeader: String,
    ):ResponseEntity<String> {
        val functionDescr = "authenticateUser"
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, functionDescr)
        try {
            val hwUser: HwUser = authSvc.logInUserWithAuthHeader(authHeader)
            val newAuth = encodeToJwt(hwUser)
            return ResponseEntity
                .ok()
                .header("Authorization", newAuth)
                .build()
        } catch (expectedException: UserNotFoundException) {
            logger.warn(loggerMsg, CommonStrings.FunctionState.FAILURE, functionDescr)
            return ResponseEntity
                .badRequest()
                .build()
        } catch (anyOtherException: Exception) {
            logger.warn(loggerMsg, CommonStrings.FunctionState.FAILURE, functionDescr)
            return ResponseEntity
                .badRequest()
                .build()
        }
    }

    @PostMapping("nw")
    fun signUpUser(
        @RequestHeader(name=authorizationHeader) authHeader: String,
    ):ResponseEntity<String>{
        val functionDescr = "signUpUser"
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, functionDescr)
        try {
            val hwUser:HwUser = authSvc.signUpUser(authHeader)
            val newAuth = encodeToJwt(hwUser)
            return ResponseEntity
                .ok()
                .header("Authorization", newAuth)
                .build()
        } catch (expectedException: UserExistsException){
            logger.warn(loggerMsg, CommonStrings.FunctionState.FAILURE, functionDescr)
            return ResponseEntity
                .badRequest()
                .build()
        } catch (anyOtherException: Exception){
            logger.warn(loggerMsg, CommonStrings.FunctionState.FAILURE, functionDescr)
            return ResponseEntity
                .badRequest()
                .build()
        }
    }
}