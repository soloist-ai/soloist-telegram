package com.sleepkqq.sololeveling.telegram.bot.service.localization.impl

import com.sleepkqq.sololeveling.telegram.bot.service.image.ImageResourceService
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard
import com.sleepkqq.sololeveling.telegram.keyboard.KeyboardAction
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.localization.Localized
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow
import java.util.*

@Service
class I18nService(
	private val messageSource: MessageSource,
	private val environment: Environment,
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
		text = getMessage(localized.localizationCode, params.ifEmpty { localized.params }, locale),
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
		text = getMessage(code, params, locale),
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
		.apply { keyboard?.let { replyMarkup(buildKeyboard(it, locale = locale)) } }
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
		caption = getMessage(localized.localizationCode, params.ifEmpty { localized.params }, locale),
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
		caption = getMessage(code, params, locale),
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
			.apply { keyboard?.let { replyMarkup(buildKeyboard(it, locale = locale)) } }
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
		text = getMessage(localized.localizationCode, params.ifEmpty { localized.params }, locale),
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
		text = getMessage(code, params, locale),
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
			.apply { keyboard?.let { replyMarkup(buildKeyboard(it, locale = locale)) } }
			.build()

	// ============ DeleteMessage ============

	fun deleteMessage(chatId: Long, messageId: Int): DeleteMessage =
		DeleteMessage.builder()
			.chatId(chatId.toString())
			.messageId(messageId)
			.build()

	// ============ Utils ============

	fun getMessage(
		code: LocalizationCode,
		params: List<Any> = emptyList(),
		locale: Locale? = null
	): String {
		val effectiveLocale = locale ?: LocaleContextHolder.getLocale()
		return messageSource.getMessage(
			code.path,
			params.toTypedArray().takeIf { it.isNotEmpty() },
			effectiveLocale
		)
	}

	fun buildKeyboard(
		keyboard: Keyboard,
		buttonsPerRow: Int = keyboard.actions.size,
		locale: Locale? = null
	): InlineKeyboardMarkup {
		val buttons = keyboard.actions.map { createButton(it, locale) }
		val rows = buttons.chunked(buttonsPerRow).map { InlineKeyboardRow(it) }
		return InlineKeyboardMarkup(rows)
	}

	private fun createButton(action: KeyboardAction, locale: Locale? = null): InlineKeyboardButton =
		when (action) {
			is KeyboardAction.Callback -> InlineKeyboardButton.builder()
				.text(getMessage(action.callbackAction.localizationCode, locale = locale))
				.callbackData(action.callbackAction.action)
				.build()

			is KeyboardAction.Url -> InlineKeyboardButton.builder()
				.text(getMessage(action.localizationCode, locale = locale))
				.url(environment.getRequiredProperty(action.urlProperty))
				.build()
		}
}
