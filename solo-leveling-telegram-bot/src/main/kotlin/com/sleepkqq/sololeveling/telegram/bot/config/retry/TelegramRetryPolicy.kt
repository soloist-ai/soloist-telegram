package com.sleepkqq.sololeveling.telegram.bot.config.retry

import org.springframework.retry.RetryContext
import org.springframework.retry.policy.SimpleRetryPolicy
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException

class TelegramRetryPolicy(maxAttempts: Int) : SimpleRetryPolicy(maxAttempts) {

	override fun canRetry(context: RetryContext): Boolean {
		val throwable = context.lastThrowable

		if (throwable is TelegramApiRequestException) {
			return when (throwable.errorCode) {
				429 -> super.canRetry(context)
				else -> false
			}
		}

		return throwable is TelegramApiException && super.canRetry(context)
	}
}
