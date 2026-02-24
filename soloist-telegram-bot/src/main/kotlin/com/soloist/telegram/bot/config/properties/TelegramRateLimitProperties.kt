package com.soloist.telegram.bot.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("app.telegram.rate-limit")
data class TelegramRateLimitProperties(
	val global: Long,
	val perChat: Long,
	val period: Duration,
	val bucketTtl: Duration,
	val globalKey: String,
	val chatKeyPrefix: String
)
