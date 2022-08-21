package com.hackerwave.auth.service

import com.hackerwave.auth.dto.LoginDto
import com.hackerwave.auth.entity.HwUser
import com.hackerwave.auth.messaging.service.KafkaSvc
import com.hackerwave.auth.repository.HwUserRepository
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.emailJwtKey
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import com.hackerwave.auth.util.CommonStrings.subJwtKey
import com.hackerwave.auth.util.JwtFunctions.decodeFieldFromJwt
import com.hackerwave.auth.util.exception.UserExistsException
import com.hackerwave.auth.util.exception.UserNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthSvc(
    val hwUserRepository: HwUserRepository,
    private val kafkaSvc: KafkaSvc
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun findUserBygIdAndEmail(email:String, gId: String):HwUser {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "logInUser")
        return hwUserRepository.findBygIdAndEmail(email, gId).orElseThrow {
            UserNotFoundException(gId)
        }
    }

    fun findUserById(id: UUID):HwUser{
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "findUserById")
        return hwUserRepository.findById(id).orElseThrow {
            throw UserNotFoundException(id.toString())
        }
    }

    fun logInUserWithAuthHeader(
        authHeader:String,
        loginDto: LoginDto
    ):HwUser{
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "logInUserWithAuthHeader")
        val gId = decodeFieldFromJwt(CommonStrings.issuerJwtKey, authHeader)
        val email = decodeFieldFromJwt(emailJwtKey, authHeader)
        val loggedInUser = findUserBygIdAndEmail( email, gId )
        kafkaSvc.submitAction(loginDto)
        return loggedInUser
    }


    fun signUpUser(
        authHeader:String,
        loginDto: LoginDto
    ):HwUser {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "signUpUser")
        try {
            val hwUser = logInUserWithAuthHeader(authHeader, loginDto)
            throw UserExistsException(hwUser.toDto().id.toString())
        } catch (exception: UserNotFoundException){
            val email = decodeFieldFromJwt(emailJwtKey, authHeader)
            val gId = decodeFieldFromJwt(subJwtKey, authHeader)
            val newUser = HwUser(email, gId)
            val savedUser = hwUserRepository.save(newUser)
            kafkaSvc.submitAction(loginDto)
            return savedUser
        }
    }

    fun findUserWithAuthHeader(
        authHeader:String,
        loginDto: LoginDto
    ):HwUser{
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "findUserWithAuthHeader")
        val localIdAsString = decodeFieldFromJwt(subJwtKey, authHeader)
        val localId = UUID.fromString(localIdAsString)
        val authorizedUser = findUserById( localId )
        kafkaSvc.submitAction(loginDto)
        return authorizedUser
    }
}