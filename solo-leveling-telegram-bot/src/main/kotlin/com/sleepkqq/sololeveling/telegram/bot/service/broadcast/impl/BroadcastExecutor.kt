package com.sleepkqq.sololeveling.telegram.bot.service.broadcast.impl

import com.sleepkqq.sololeveling.proto.user.LocaleUserView
import com.sleepkqq.sololeveling.telegram.bot.extensions.SendMessage
import com.sleepkqq.sololeveling.telegram.bot.extensions.withReplyMarkup
import com.sleepkqq.sololeveling.telegram.bot.service.localization.impl.I18nService
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageSender
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard
import com.sleepkqq.sololeveling.telegram.model.entity.broadcast.dto.ScheduledBroadcastView
import com.sleepkqq.sololeveling.telegram.model.entity.localziation.LocalizedMessage
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.util.Locale

@Component
class BroadcastExecutor(
	private val telegramMessageSender: TelegramMessageSender,
	private val i18nService: I18nService
) {

	private val log = LoggerFactory.getLogger(javaClass)

	fun execute(broadcast: ScheduledBroadcastView, users: List<LocaleUserView>): ExecutionResult {
		val messagesMap = broadcast.messages
			.associateBy { Locale.forLanguageTag(it.locale.name.lowercase()) }
			.mapValues { it.value.toEntity() }

		var success = 0
		var failed = 0

		runBlocking {
			users.map { async(Dispatchers.IO) { sendTo(it, messagesMap, broadcast.fileId) } }
				.awaitAll().forEach { sent -> if (sent) success++ else failed++ }
		}

		return ExecutionResult(users.size, success, failed)
	}

	private fun sendTo(
		user: LocaleUserView,
		messagesMap: Map<Locale, LocalizedMessage>,
		fileId: String?
	): Boolean = try {
		val locale = Locale.forLanguageTag(user.locale)
		val message = messagesMap[locale]
			?: messagesMap[Locale.ENGLISH]
			?: messagesMap.values.first()

		val keyboard = i18nService.buildKeyboard(Keyboard.MINI_APP_LINK, locale = locale)

		if (fileId != null) {
			telegramMessageSender.send(
				SendPhoto.builder()
					.chatId(user.id)
					.photo(InputFile(fileId))
					.caption(message.text())
					.replyMarkup(keyboard)
					.build()
			)
		} else {
			telegramMessageSender.send(
				SendMessage(user.id, message.text())
					.withReplyMarkup(keyboard)
			)
		}

		true

	} catch (e: Exception) {
		log.warn("Failed to send message to user ${user.id}", e)
		false
	}

	data class ExecutionResult(
		val total: Int,
		val totalSuccess: Int,
		val totalFailed: Int
	)
}
