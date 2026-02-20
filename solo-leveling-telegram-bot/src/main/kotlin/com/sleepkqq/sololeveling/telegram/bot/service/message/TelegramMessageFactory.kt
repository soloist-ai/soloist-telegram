package com.sleepkqq.sololeveling.telegram.bot.service.message

import com.sleepkqq.sololeveling.telegram.bot.service.image.ImageResourceService
import com.sleepkqq.sololeveling.telegram.bot.service.localization.impl.I18nService
import com.sleepkqq.sololeveling.telegram.bot.service.localization.impl.PhotoSource
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.localization.Localized
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.util.Locale

@Service
class TelegramMessageFactory(
	private val i18nService: I18nService,
	private val imageResourceService: ImageResourceService
) {

	private companion object {
		const val HTML_MODE = "HTML"
	}

	// ============ SendMessage ============

	fun sendMessage(
		chatId: Long,
		localized: Localized,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): SendMessage = sendMessage(
		chatId = chatId,
		text = i18nService.getMessage(
			localized.localizationCode,
			params.ifEmpty { localized.params },
			locale
		),
		keyboard = keyboard ?: localized.keyboard,
		locale = locale
	)

	fun sendMessage(
		chatId: Long,
		code: LocalizationCode,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): SendMessage = sendMessage(
		chatId = chatId,
		text = i18nService.getMessage(code, params, locale),
		keyboard = keyboard,
		locale = locale
	)

	fun sendMessage(
		chatId: Long,
		text: String,
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): SendMessage = SendMessage.builder()
		.chatId(chatId.toString())
		.text(text)
		.parseMode(HTML_MODE)
		.apply { keyboard?.let { replyMarkup(i18nService.buildKeyboard(it, locale = locale)) } }
		.build()

	// ============ SendPhoto ============

	fun sendPhoto(
		chatId: Long,
		source: PhotoSource,
		localized: Localized,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): SendPhoto = sendPhoto(
		chatId = chatId,
		source = source,
		caption = i18nService.getMessage(
			localized.localizationCode,
			params.ifEmpty { localized.params },
			locale
		),
		keyboard = keyboard ?: localized.keyboard,
		locale = locale
	)

	fun sendPhoto(
		chatId: Long,
		source: PhotoSource,
		code: LocalizationCode,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): SendPhoto = sendPhoto(
		chatId = chatId,
		source = source,
		caption = i18nService.getMessage(code, params, locale),
		keyboard = keyboard,
		locale = locale
	)

	fun sendPhoto(
		chatId: Long,
		source: PhotoSource,
		caption: String,
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): SendPhoto {
		val photo = when (source) {
			is PhotoSource.FileId -> InputFile(source.id)
			is PhotoSource.Resource -> InputFile(
				imageResourceService.getPhotoStream(source.image),
				source.image.fileName
			)
		}

		return SendPhoto.builder()
			.chatId(chatId.toString())
			.photo(photo)
			.caption(caption)
			.parseMode(HTML_MODE)
			.apply {
				keyboard?.let {
					replyMarkup(i18nService.buildKeyboard(it, locale = locale))
				}
			}
			.build()
	}

	// ============ EditMessageText ============

	fun editMessageText(
		chatId: Long,
		messageId: Int,
		localized: Localized,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): EditMessageText = editMessageText(
		chatId = chatId,
		messageId = messageId,
		text = i18nService.getMessage(
			localized.localizationCode,
			params.ifEmpty { localized.params },
			locale
		),
		keyboard = keyboard ?: localized.keyboard,
		locale = locale
	)

	fun editMessageText(
		chatId: Long,
		messageId: Int,
		code: LocalizationCode,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): EditMessageText = editMessageText(
		chatId = chatId,
		messageId = messageId,
		text = i18nService.getMessage(code, params, locale),
		keyboard = keyboard,
		locale = locale
	)

	fun editMessageText(
		chatId: Long,
		messageId: Int,
		text: String,
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): EditMessageText =
		EditMessageText.builder()
			.chatId(chatId.toString())
			.messageId(messageId)
			.text(text)
			.parseMode(HTML_MODE)
			.apply {
				keyboard?.let {
					replyMarkup(i18nService.buildKeyboard(it, locale = locale))
				}
			}
			.build()

	// ============ DeleteMessage ============

	fun deleteMessage(chatId: Long, messageId: Int): DeleteMessage =
		DeleteMessage.builder()
			.chatId(chatId.toString())
			.messageId(messageId)
			.build()
}