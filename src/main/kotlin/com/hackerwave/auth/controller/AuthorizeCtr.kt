package com.hackerwave.auth.controller

import com.hackerwave.auth.dto.LoginDto
import com.hackerwave.auth.entity.HwUser
import com.hackerwave.auth.service.AuthSvc
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import com.hackerwave.auth.util.exception.UserNotFoundException
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@Tag(name="Authorization API", description = "Determine access to resources using USER ID")
@RestController("api/\${custom.api.prefix.version}/\${custom.api.prefix.authorization}/")
class AuthorizeCtr(
    private val authSvc: AuthSvc
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("ex")
    fun authorizeUser(
        @RequestHeader(name= CommonStrings.authorizationHeader) authHeader: String,
        @RequestBody loginDto: LoginDto,
    ): ResponseEntity<String> {
        val functionDescr = "authorizeUser"
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, functionDescr)
        try {
            val hwUser: HwUser = authSvc.findUserWithAuthHeader(authHeader, loginDto)
            val descr = hwUser.toDto().id
            logger.info(loggerMsg, CommonStrings.FunctionState.PERMIT, descr)
            return ResponseEntity
                .ok()
                .build()
        } catch (expectedException: UserNotFoundException){
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