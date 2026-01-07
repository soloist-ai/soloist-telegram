package com.sleepkqq.sololeveling.telegram.bot.service.localization

import com.sleepkqq.sololeveling.telegram.bot.extensions.SendMessage
import com.sleepkqq.sololeveling.telegram.bot.extensions.withReplyMarkup
import com.sleepkqq.sololeveling.telegram.bot.service.image.ImageResourceService
import com.sleepkqq.sololeveling.telegram.image.Image
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard
import com.sleepkqq.sololeveling.telegram.keyboard.KeyboardAction
import com.sleepkqq.sololeveling.telegram.localization.Localized
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
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

	private fun getMessageInternal(
		code: LocalizationCode,
		params: List<Any> = listOf(),
		locale: Locale? = null
	): String {
		val effectiveLocale = locale ?: LocaleContextHolder.getLocale()

		if (params.isEmpty()) {
			return messageSource.getMessage(code.path, null, effectiveLocale)
		}

		val args = params.toTypedArray()
		return messageSource.getMessage(code.path, args, effectiveLocale)
	}

	private fun buildKeyboard(
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
			is KeyboardAction.Callback ->
				InlineKeyboardButton.builder()
					.text(getMessageInternal(action.callbackAction.localizationCode, locale = locale))
					.callbackData(action.callbackAction.action)
					.build()

			is KeyboardAction.Url -> {
				val url = environment.getRequiredProperty(action.urlProperty)
				InlineKeyboardButton.builder()
					.text(getMessageInternal(action.localizationCode, locale = locale))
					.url(url)
					.build()
			}
		}

	// ============ SendMessage ============

	fun sendMessage(
		chatId: Long,
		localized: Localized,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): SendMessage {
		val effectiveParams = params.ifEmpty { localized.params }
		val effectiveKeyboard = keyboard ?: localized.keyboard

		val message = SendMessage(
			chatId,
			getMessageInternal(localized.localizationCode, effectiveParams, locale)
		)

		return effectiveKeyboard
			?.let { message.withReplyMarkup(buildKeyboard(it, locale = locale)) }
			?: message
	}

	fun sendMessage(
		chatId: Long,
		code: LocalizationCode,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): SendMessage {
		val message = SendMessage(chatId, getMessageInternal(code, params, locale))
		return keyboard?.let { message.withReplyMarkup(buildKeyboard(it, locale = locale)) } ?: message
	}

	// ============ SendPhoto ============

	fun sendPhoto(
		chatId: Long,
		image: Image,
		localized: Localized,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): SendPhoto {
		val effectiveParams = params.ifEmpty { localized.params }
		val effectiveKeyboard = keyboard ?: localized.keyboard

		val photoStream = imageResourceService.getPhotoStream(image)
		val sendPhoto = SendPhoto.builder()
			.chatId(chatId.toString())
			.photo(InputFile(photoStream, image.fileName))
			.caption(getMessageInternal(localized.localizationCode, effectiveParams, locale))
			.parseMode(HTML_MODE)

		return effectiveKeyboard
			?.let { sendPhoto.replyMarkup(buildKeyboard(it, locale = locale)).build() }
			?: sendPhoto.build()
	}

	fun sendPhoto(
		chatId: Long,
		image: Image,
		code: LocalizationCode,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): SendPhoto {
		val photoStream = imageResourceService.getPhotoStream(image)
		val sendPhoto = SendPhoto.builder()
			.chatId(chatId.toString())
			.photo(InputFile(photoStream, image.fileName))
			.caption(getMessageInternal(code, params, locale))
			.parseMode(HTML_MODE)

		return keyboard
			?.let { sendPhoto.replyMarkup(buildKeyboard(it, locale = locale)).build() }
			?: sendPhoto.build()
	}

	// ============ EditMessageText ============

	fun editMessageText(
		chatId: Long,
		messageId: Int,
		localized: Localized,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): EditMessageText {
		val effectiveParams = params.ifEmpty { localized.params }
		val effectiveKeyboard = keyboard ?: localized.keyboard

		val edit = EditMessageText.builder()
			.chatId(chatId.toString())
			.messageId(messageId)
			.text(getMessageInternal(localized.localizationCode, effectiveParams, locale))

		return effectiveKeyboard
			?.let { edit.replyMarkup(buildKeyboard(it, locale = locale)).build() }
			?: edit.build()
	}

	fun editMessageText(
		chatId: Long,
		messageId: Int,
		code: LocalizationCode,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		locale: Locale? = null
	): EditMessageText {
		val edit = EditMessageText.builder()
			.chatId(chatId.toString())
			.messageId(messageId)
			.text(getMessageInternal(code, params, locale))

		return keyboard
			?.let { edit.replyMarkup(buildKeyboard(it, locale = locale)).build() }
			?: edit.build()
	}

	// ============ DeleteMessage ============

	fun deleteMessage(chatId: Long, messageId: Int): DeleteMessage {
		return DeleteMessage.builder()
			.chatId(chatId.toString())
			.messageId(messageId)
			.build()
	}
}
