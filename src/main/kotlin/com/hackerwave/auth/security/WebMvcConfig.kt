package com.hackerwave.auth.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.firewall.StrictHttpFirewall
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
class MvcConfig:WebMvcConfigurer {
    private val permittedVerbs = arrayOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods(*permittedVerbs)
            .allowCredentials(true)
    }

    @Bean
    fun configureFireWall():StrictHttpFirewall{
        val firewall = StrictHttpFirewall()
        firewall.setAllowedHttpMethods(permittedVerbs.toList())
        return firewall
    }
}