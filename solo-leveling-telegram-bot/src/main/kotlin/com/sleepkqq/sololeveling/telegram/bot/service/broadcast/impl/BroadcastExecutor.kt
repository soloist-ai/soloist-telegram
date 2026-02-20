package com.sleepkqq.sololeveling.telegram.bot.service.broadcast.impl

import com.sleepkqq.sololeveling.proto.user.LocaleUserView
import com.sleepkqq.sololeveling.telegram.bot.service.localization.impl.PhotoSource
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageSender
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard
import com.sleepkqq.sololeveling.telegram.model.entity.broadcast.dto.ScheduledBroadcastView
import com.sleepkqq.sololeveling.telegram.model.entity.localziation.LocalizedMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class BroadcastExecutor(
	private val telegramMessageSender: TelegramMessageSender,
	private val telegramMessageFactory: TelegramMessageFactory
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

		if (fileId != null) {
			val sendPhoto = telegramMessageFactory.sendPhoto(
				chatId = user.id,
				source = PhotoSource.FileId(fileId),
				caption = message.text(),
				keyboard = Keyboard.MINI_APP_LINK,
				locale = locale
			)
			telegramMessageSender.send(sendPhoto)

		} else {
			val sendMessage = telegramMessageFactory.sendMessage(
				chatId = user.id,
				text = message.text(),
				keyboard = Keyboard.MINI_APP_LINK,
				locale = locale
			)
			telegramMessageSender.send(sendMessage)
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
