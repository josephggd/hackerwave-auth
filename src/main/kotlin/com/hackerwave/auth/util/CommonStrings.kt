package com.hackerwave.auth.util

import java.time.format.DateTimeFormatter

object CommonStrings {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss")
    // logging
    const val loggerMsg = "|| STATE:{} | DESCR: {} ||"
    const val userNotFoundExceptionMsg = "USER %s NOT FOUND"
    const val userExistsExceptionMsg = "USER %s ALREADY EXISTS"
    const val badJwtExceptionMsg = "BAD JWT"
    enum class FunctionState {
        ATTEMPT,
        PERMIT,
        FAILURE
    }
    // headers/security
    const val authorizationHeader = "Authorization"
    const val issuerJwtKey = "iss"
    const val issuerOutgoingJwtValue = "auth"
    const val emailJwtKey = "email"
    const val subJwtKey = "sub"
    const val noCredentials = "NoCredentials"
    // kafka
    const val gpByDateTopic = "action-topic"
    const val gpByAllTopic = "date-topic"
    enum class UserAction {
        CREATE_ACCT,
        DELETE_ACCT,
        LOGIN,
        CREATE_POST,
        DELETE_POST,
        LIKE_POST,
        UNLIKE_POST,
    }
    enum class Grouping {
        ACTION,
        DATE,
    }
    const val gpByDateStore = "action-store"
    const val gpByAllStore = "date-store"
}