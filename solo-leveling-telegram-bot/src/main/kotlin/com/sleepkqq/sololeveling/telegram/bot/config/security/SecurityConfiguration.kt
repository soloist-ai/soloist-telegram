package com.sleepkqq.sololeveling.telegram.bot.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Suppress("unused")
@Configuration
@EnableWebSecurity
class SecurityConfiguration(
	private val telegramSecurityFilter: TelegramWebhookSecurityFilter
) {

	@Bean
	@Throws(Exception::class)
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http
		.csrf { it.disable() }
		.addFilterBefore(
			telegramSecurityFilter,
			UsernamePasswordAuthenticationFilter::class.java
		)
		.authorizeHttpRequests { it.anyRequest().permitAll() }
		.formLogin { it.disable() }
		.build()
}
