package com.batch14.usermanagementservice.config

import org.apache.catalina.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.SecurityFilterChain
import org.springframework.http.HttpMethod


//@EnableMethodSecurity
//@Configuration
//class SecurityConfig {
//    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        http
//            .csrf { it.disable() }
//            .authorizeHttpRequests {
//                it.requestMatchers(HttpMethod.DELETE, "/user-service/v1/users/*/hard-delete").hasRole("ADMIN")
//                it.anyRequest().permitAll()
//            }
//            .httpBasic { }
//        return http.build()
//    }
//}