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
import java.time.temporal.TemporalUnit
import java.util.*

@SpringBootApplication
class AuthApplication

fun main(args: Array<String>) {
	runApplication<AuthApplication>(*args)
}

@Component
class CommandExample @Autowired constructor(private val svc:KafkaSvc) : CommandLineRunner {
	override fun run(vararg args: String?) {
		val howMany = listOf(1,2,3,4,5)
		val actionChoices = enumValues<CommonStrings.UserAction>()
//		(val date: String,
//		val time: String,
//		val action:CommonStrings.UserAction)
		val dateChoices = listOf("2022-02-04","2022-02-07","2022-02-01","2022-02-15","2022-02-28")
		val timeChoices = howMany.map { Instant.now().toString() }
		howMany.map {
			svc.submitAction(
				ActionDto(
					dateChoices.asSequence().shuffled().find { true }!!,
					timeChoices.asSequence().shuffled().find { true }!!,
					actionChoices.asSequence().shuffled().find { true }!!
				)
			)
		}
	}
}