package com.sleepkqq.sololeveling.telegram.bot.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.telegram.bot")
data class TelegramBotProperties(
	val token: String,
	val webhook: Webhook,
	val api: Api
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
}
