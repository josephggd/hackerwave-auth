package com.hackerwave.auth

import com.hackerwave.auth.dto.ActionDto
import com.hackerwave.auth.messaging.service.KafkaSvc
import com.hackerwave.auth.util.CommonStrings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import java.time.Instant

@SpringBootApplication
class AuthApplication

fun main(args: Array<String>) {
	runApplication<AuthApplication>(*args)
}

@Component
class CommandExample @Autowired constructor(private val svc:KafkaSvc) : CommandLineRunner {
	override fun run(vararg args: String?) {
		val actionChoices = enumValues<CommonStrings.UserAction>()
		actionChoices.map {
			svc.submitAction(it)
		}
	}
}