package com.hackerwave.auth.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.hackerwave.auth.entity.HwUser
import com.hackerwave.auth.util.CommonStrings.authorizationHeader
import com.hackerwave.auth.util.CommonStrings.emailJwtKey
import com.hackerwave.auth.util.CommonStrings.issuerJwtKey
import com.hackerwave.auth.util.CommonStrings.issuerOutgoingJwtValue
import com.hackerwave.auth.util.CommonStrings.subJwtKey
import com.hackerwave.auth.util.exception.BadJwtException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import javax.servlet.http.HttpServletRequest

object JwtFunctions {
    private val logger: Logger = LoggerFactory.getLogger(JwtFunctions::class.java)

    @Value("\${custom.jwt.secret}")
    private lateinit var secret:String

    private val algorithm = Algorithm.HMAC512(secret)

    private fun getJwtFromRequest(request: HttpServletRequest?): String {
        logger.info("getJwtFromRequest")
        return request?.getHeader(authorizationHeader) ?: ""
    }

    fun decodeFieldFromJwt(field:String, jwt: String): String {
        logger.info("decodeFieldFromJwt")
        try {
            return JWT.decode(jwt).getClaim(field).asString()
        } catch (exception: Exception){
            logger.warn(exception.message)
            throw BadJwtException()
        }
    }

    fun decodeFieldFromJwt(field:String, request: HttpServletRequest?): String {
        logger.info("decodeFieldFromJwt")
        try {
            val jwt = getJwtFromRequest(request)
            return decodeFieldFromJwt(field, jwt)
        } catch (exception: Exception){
            logger.warn(exception.message)
            throw BadJwtException()
        }
    }

    fun encodeToJwt(hwUser: HwUser):String{
        val dto = hwUser.toDto()
        return JWT.create()
            .withClaim(issuerJwtKey, issuerOutgoingJwtValue)
            .withClaim(subJwtKey, dto.id.toString())
            .withClaim(emailJwtKey, dto.email)
            .sign(algorithm)
    }
}