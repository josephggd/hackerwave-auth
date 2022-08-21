package com.hackerwave.auth.service

import com.hackerwave.auth.entity.HwUser
import com.hackerwave.auth.repository.HwUserRepository
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.Companion.loggerMsg
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AuthenticateSvc(
    val hwUserRepository: HwUserRepository
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun logInUser(email:String, gId: String):HwUser {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "logInUser")
        return hwUserRepository.findByGIdAndEmail(email, gId).orElseThrow {
            IllegalArgumentException("USER NOT FOUND")
        }
    }
}