package com.hackerwave.auth.controller

import com.hackerwave.auth.service.AuthorizeSvc
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthorizeCtr(
    private val authorizeSvc: AuthorizeSvc
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    // TODO: Add security to parse/extend userId cookie
}