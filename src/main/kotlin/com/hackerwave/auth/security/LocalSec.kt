package com.hackerwave.auth.security

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = ["security.enabled"], havingValue = "false")
class LocalSec {
    @Bean
    @Throws(Exception::class)
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain? {
        httpSecurity
            .authorizeRequests()
            .antMatchers("/**").permitAll()
        httpSecurity.formLogin().disable()
        httpSecurity.httpBasic().disable()
        httpSecurity.csrf().disable()
        httpSecurity.headers().frameOptions().disable()
        httpSecurity.cors()
        return httpSecurity.build()
    }
}