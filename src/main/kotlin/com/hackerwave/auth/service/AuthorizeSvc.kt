package com.hackerwave.auth.service

import com.hackerwave.auth.entity.HwUser
import com.hackerwave.auth.repository.HwUserRepository
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.Companion.loggerMsg
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthorizeSvc(
    val hwUserRepository: HwUserRepository
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun findUserById(id:UUID):HwUser{
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "findUserById")
        return hwUserRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("USER NOT FOUND")
        }
    }
}