package com.hackerwave.auth.controller

import com.hackerwave.auth.entity.HwUser
import com.hackerwave.auth.service.AuthSvc
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController("api/\${custom.api.prefix.version}/DUMMY/")
class DummyCtr(
    private val authSvc: AuthSvc
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("DUMMY")
    fun authorizeUser(
        @RequestHeader(name= CommonStrings.authorizationHeader) authHeader: String,
    ): ResponseEntity<String> {
        val functionDescr = "DUMMY"
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, functionDescr)
        val hwUser: HwUser = authSvc.authorizeExistingUser(authHeader)
        val descr = hwUser.toDto().id
        logger.info(loggerMsg, CommonStrings.FunctionState.PERMIT, descr)
        return ResponseEntity
            .ok()
            .build()
    }
}