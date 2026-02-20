package com.sleepkqq.sololeveling.telegram.bot.config.retry

import com.sleepkqq.sololeveling.telegram.bot.config.properties.TelegramRetryProperties
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.RetryListener
import org.springframework.retry.support.RetryTemplate

@Configuration
class RetryConfig(
	private val telegramRetryProperties: TelegramRetryProperties
) {

	private val log = LoggerFactory.getLogger(RetryConfig::class.java)

	@Bean
	fun telegramRetryTemplate(): RetryTemplate = RetryTemplate.builder()
		.maxAttempts(telegramRetryProperties.maxAttempts)
		.exponentialBackoff(
			telegramRetryProperties.initialInterval,
			telegramRetryProperties.multiplier,
			telegramRetryProperties.maxInterval,
			true
		)
		.customPolicy(TelegramRetryPolicy(telegramRetryProperties.maxAttempts))
		.withListener(object : RetryListener {
			override fun <T, E : Throwable> onError(
				context: RetryContext,
				callback: RetryCallback<T, E>,
				throwable: Throwable
			) {
				log.warn(
					"Telegram API error, attempt {}/{}: {}",
					context.retryCount + 1, telegramRetryProperties.maxAttempts, throwable.message
				)
			}
		})
		.build()
}
