package com.soloist.telegram.bot.service.message

import org.slf4j.LoggerFactory
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.generics.TelegramClient

@Service
class TelegramMessageSender(
	private val telegramClient: TelegramClient,
	private val telegramRateLimiter: TelegramRateLimiter,
	private val telegramRetryTemplate: RetryTemplate
) {

	private val log = LoggerFactory.getLogger(TelegramMessageSender::class.java)

	fun send(sendMessage: SendMessage) =
		execute(sendMessage.chatId) { telegramClient.execute(sendMessage) }

	fun send(sendPhoto: SendPhoto) =
		execute(sendPhoto.chatId) { telegramClient.execute(sendPhoto) }

	private fun execute(chatId: String, action: () -> Unit) {
		telegramRateLimiter.consume(chatId)
		telegramRetryTemplate.execute<Unit, TelegramApiException> {
			runCatching { action() }
				.onFailure { e ->
					if (e is TelegramApiRequestException && e.errorCode == 429) {
						val waitMs = (e.parameters?.retryAfter?.toLong() ?: 5L) * 1000L
						log.warn("Telegram 429, retry_after={}ms [chat={}]", waitMs, chatId)
						Thread.sleep(waitMs)
					}
					throw e
				}
		}
	}
}
