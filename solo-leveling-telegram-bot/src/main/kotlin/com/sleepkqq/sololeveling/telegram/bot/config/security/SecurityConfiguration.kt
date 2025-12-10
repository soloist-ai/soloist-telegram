package com.sleepkqq.sololeveling.telegram.bot.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Suppress("unused")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
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

	@Bean
	fun roleHierarchy(): RoleHierarchy = RoleHierarchyImpl.fromHierarchy(
		"""
					DEVELOPER > USER
					MANAGER > USER
					ADMIN > DEVELOPER
					ADMIN > MANAGER
					""".trimIndent()
	)
}
