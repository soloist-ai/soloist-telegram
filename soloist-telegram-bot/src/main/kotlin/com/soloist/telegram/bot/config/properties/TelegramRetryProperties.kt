package com.soloist.telegram.bot.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "app.telegram.retry")
data class TelegramRetryProperties(
	val maxAttempts: Int,
	val initialInterval: Duration,
	val multiplier: Double,
	val maxInterval: Duration
)
