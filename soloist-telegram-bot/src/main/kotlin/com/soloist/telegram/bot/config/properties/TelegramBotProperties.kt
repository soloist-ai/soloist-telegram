package com.soloist.telegram.bot.config.properties

import com.soloist.telegram.bot.model.UserRole
import com.soloist.telegram.callback.CallbackData
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.telegram.bot")
data class TelegramBotProperties(
	val token: String,
	val webhook: Webhook,
	val api: Api,
	val commands: Map<String, HandlerConfig> = emptyMap(),
	val callbacks: Map<CallbackData, HandlerConfig> = emptyMap()
) {

	data class Webhook(
		val host: String,
		val path: String,
		val secretToken: String
	) {
		val url: String
			get() = "$host/$path"
	}

	data class Api(
		val url: String
	)

	data class HandlerConfig(
		val enabled: Boolean = true,
		val role: UserRole = UserRole.USER
	)
}