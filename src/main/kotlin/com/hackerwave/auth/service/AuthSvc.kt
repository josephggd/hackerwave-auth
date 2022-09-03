package com.hackerwave.auth.service

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
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "findUserBygIdAndEmail")
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

    fun saveUser(authHeader:String):HwUser {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "saveUser")
        val email = decodeFieldFromJwt(emailJwtKey, authHeader)
        val gId = decodeFieldFromJwt(subJwtKey, authHeader)
        val newUser = HwUser(email, gId)
        val savedUser = hwUserRepository.save(newUser)
        kafkaSvc.submitAction(CommonStrings.UserAction.CREATE_ACCT)
        return savedUser
    }

    fun deleteUser(authHeader:String) {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "deleteUser")
        val email = decodeFieldFromJwt(emailJwtKey, authHeader)
        val gId = decodeFieldFromJwt(subJwtKey, authHeader)
        val newUser = HwUser(email, gId)
        hwUserRepository.delete(newUser)
        kafkaSvc.submitAction(CommonStrings.UserAction.DELETE_ACCT)
    }

    fun authenticateUnauthenticatedUser(authHeader:String):HwUser{
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "authenticateUnauthenticatedUser")
        val gId = decodeFieldFromJwt(CommonStrings.issuerJwtKey, authHeader)
        val email = decodeFieldFromJwt(emailJwtKey, authHeader)
        val loggedInUser = findUserBygIdAndEmail( email, gId )
        kafkaSvc.submitAction(CommonStrings.UserAction.LOGIN)
        return loggedInUser
    }


    fun signUpNewUser(authHeader:String):HwUser {
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "signUpNewUser")
        try {
            val hwUser = authenticateUnauthenticatedUser(authHeader)
            throw UserExistsException(hwUser.toDto().id.toString())
        } catch (exception: UserNotFoundException){
            return saveUser(authHeader)
        }
    }

    fun authorizeExistingUser(authHeader:String):HwUser{
        logger.info(loggerMsg, CommonStrings.FunctionState.ATTEMPT, "authorizeExistingUser")
        val localIdAsString = decodeFieldFromJwt(subJwtKey, authHeader)
        val localId = UUID.fromString(localIdAsString)
        val authorizedUser = findUserById( localId )
        kafkaSvc.submitAction(CommonStrings.UserAction.LOGIN)
        return authorizedUser
    }
}