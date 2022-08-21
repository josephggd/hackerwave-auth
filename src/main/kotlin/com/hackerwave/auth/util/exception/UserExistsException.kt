package com.hackerwave.auth.util.exception

import com.hackerwave.auth.util.CommonStrings.userExistsExceptionMsg

class UserExistsException(private val affectedUser:String) : IllegalArgumentException() {
    override val message: String
        get() = userExistsExceptionMsg.format(affectedUser)
}