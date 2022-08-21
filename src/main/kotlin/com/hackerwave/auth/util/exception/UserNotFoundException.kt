package com.hackerwave.auth.util.exception

import com.hackerwave.auth.util.CommonStrings.userNotFoundExceptionMsg

class UserNotFoundException(private val affectedUser:String) : IllegalArgumentException() {
    override val message: String
        get() = userNotFoundExceptionMsg.format(affectedUser)
}