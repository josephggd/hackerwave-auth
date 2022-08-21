package com.hackerwave.auth.util.exception

import com.hackerwave.auth.util.CommonStrings.badJwtExceptionMsg

class BadJwtException : IllegalArgumentException() {
    override val message: String
        get() = badJwtExceptionMsg
}