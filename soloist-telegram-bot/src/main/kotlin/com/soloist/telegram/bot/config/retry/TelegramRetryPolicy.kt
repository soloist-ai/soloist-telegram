package com.soloist.telegram.bot.config.retry

import org.slf4j.LoggerFactory
import org.springframework.retry.RetryContext
import org.springframework.retry.policy.SimpleRetryPolicy
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException

class TelegramRetryPolicy(maxAttempts: Int) : SimpleRetryPolicy(maxAttempts) {

	private val log = LoggerFactory.getLogger(javaClass)

	override fun canRetry(context: RetryContext): Boolean {
		val throwable = context.lastThrowable
			?: return super.canRetry(context)

		val willRetry = if (throwable is TelegramApiRequestException) {
			when (throwable.errorCode) {
				429 -> super.canRetry(context)
				else -> false
			}
		} else {
			throwable is TelegramApiException && super.canRetry(context)
		}

		if (willRetry) {
			log.warn(
				"Telegram API error, retrying {}/{}: {}",
				context.retryCount, maxAttempts, throwable.message
			)
		}

		return willRetry
	}
}
