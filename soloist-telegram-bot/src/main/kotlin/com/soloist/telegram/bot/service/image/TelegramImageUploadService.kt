package com.soloist.telegram.bot.service.image

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.generics.TelegramClient
import java.io.ByteArrayInputStream

@Service
class TelegramImageUploadService(
	private val telegramClient: TelegramClient
) {

	private val log = LoggerFactory.getLogger(javaClass)

	fun uploadAndGetFileId(chatId: Long, imageBytes: ByteArray, filename: String): String {
		val inputFile = InputFile(ByteArrayInputStream(imageBytes), filename)
		val sendPhoto = SendPhoto.builder()
			.chatId(chatId.toString())
			.photo(inputFile)
			.build()

		val sentMessage = telegramClient.execute(sendPhoto)
		val fileId = sentMessage.photo.last().fileId

		log.debug("Uploaded image to Telegram chat={}, fileId={}", chatId, fileId)

		try {
			telegramClient.execute(
				DeleteMessage.builder()
					.chatId(chatId.toString())
					.messageId(sentMessage.messageId)
					.build()
			)
		} catch (e: Exception) {
			log.warn("Failed to delete temp upload message chat={} messageId={}", chatId, sentMessage.messageId, e)
		}

		return fileId
	}
}
