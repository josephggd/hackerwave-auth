package com.hackerwave.auth.service

import com.hackerwave.auth.entity.HwUser
import com.hackerwave.auth.repository.HwUserRepository
import com.hackerwave.auth.util.CommonStrings
import com.hackerwave.auth.util.CommonStrings.emailJwtKey
import com.hackerwave.auth.util.CommonStrings.loggerMsg
import com.hackerwave.auth.util.CommonStrings.subJwtKey
import com.hackerwave.auth.util.JwtFunctions.decodeFieldFromJwt
import com.hackerwave.auth.util.JwtFunctions.getJwtFromAuthHeader
import com.hackerwave.auth.util.exception.UserExistsException
import com.hackerwave.auth.util.exception.UserNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthSvc(
    val hwUserRepository: HwUserRepository
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun logInUser(email:String, gId: String):HwUser {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "logInUser")
        return hwUserRepository.findByGIdAndEmail(email, gId).orElseThrow {
            UserNotFoundException(gId)
        }
    }

    fun logInUserWithAuthHeader(authHeader:String):HwUser{
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "logInUserWithAuthHeader")
        val token = getJwtFromAuthHeader(authHeader)
        val gId = decodeFieldFromJwt(CommonStrings.issuerJwtKey, token)
        val email = decodeFieldFromJwt(CommonStrings.emailJwtKey, token)
        return logInUser( email, gId )
    }


    fun signUpUser(authHeader:String):HwUser {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "signUpUser")
        try {
            val hwUser = logInUserWithAuthHeader(authHeader)
            throw UserExistsException(hwUser.toDto().id.toString())
        } catch (exception: UserNotFoundException){
            val email = decodeFieldFromJwt(emailJwtKey, authHeader)
            val gId = decodeFieldFromJwt(subJwtKey, authHeader)
            val hwUser = HwUser(email, gId)
            return hwUserRepository.save(hwUser)
        }
    }

    fun findUserById(id: UUID):HwUser{
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "findUserById")
        return hwUserRepository.findById(id).orElseThrow {
            throw UserNotFoundException(id.toString())
        }
    }

    fun findUserWithAuthHeader(authHeader:String):HwUser{
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "findUserWithAuthHeader")
        val token = getJwtFromAuthHeader(authHeader)
        val localIdAsString = decodeFieldFromJwt(subJwtKey, token)
        val localId = UUID.fromString(localIdAsString)
        return findUserById( localId )
    }
}