package com.sleepkqq.sololeveling.telegram.bot.service.localization

import com.sleepkqq.sololeveling.telegram.bot.extensions.SendMessage
import com.sleepkqq.sololeveling.telegram.bot.extensions.withReplyMarkup
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard
import com.sleepkqq.sololeveling.telegram.keyboard.KeyboardAction
import com.sleepkqq.sololeveling.telegram.localization.Localized
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow

@Service
class I18nService(
	private val messageSource: MessageSource,
	private val environment: Environment
) {

	private fun getMessageInternal(
		code: LocalizationCode,
		params: Map<String, Any?> = emptyMap()
	): String {
		val args = if (params.isEmpty()) null else params.values.toTypedArray()
		return messageSource.getMessage(code.path, args, LocaleContextHolder.getLocale())
	}

	private fun buildKeyboard(
		keyboard: Keyboard,
		buttonsPerRow: Int = keyboard.actions.size
	): InlineKeyboardMarkup {
		val buttons = keyboard.actions.map { createButton(it) }
		val rows = buttons.chunked(buttonsPerRow).map { InlineKeyboardRow(it) }
		return InlineKeyboardMarkup(rows)
	}

	private fun createButton(action: KeyboardAction): InlineKeyboardButton =
		when (action) {
			is KeyboardAction.Callback ->
				InlineKeyboardButton.builder()
					.text(getMessageInternal(action.callbackAction.localizationCode))
					.callbackData(action.callbackAction.action)
					.build()

			is KeyboardAction.Url -> {
				val url = environment.getRequiredProperty(action.urlProperty)
				InlineKeyboardButton.builder()
					.text(getMessageInternal(action.localizationCode))
					.url(url)
					.build()
			}
		}

	// ============ SendMessage ============

	fun sendMessage(
		chatId: Long,
		localized: Localized,
		params: Map<String, Any> = emptyMap(),
		keyboard: Keyboard? = null
	): SendMessage {

		val effectiveParams = params.ifEmpty { localized.params }
		val effectiveKeyboard = keyboard ?: localized.keyboard

		val message = SendMessage(
			chatId,
			getMessageInternal(localized.localizationCode, effectiveParams)
		)

		return effectiveKeyboard
			?.let { message.withReplyMarkup(buildKeyboard(it)) }
			?: message
	}

	fun sendMessage(
		chatId: Long,
		code: LocalizationCode,
		params: Map<String, Any> = emptyMap(),
		keyboard: Keyboard? = null
	): SendMessage {
		val message = SendMessage(chatId, getMessageInternal(code, params))
		return keyboard?.let { message.withReplyMarkup(buildKeyboard(it)) } ?: message
	}

	// ============ EditMessageText ============

	fun editMessageText(
		chatId: Long,
		messageId: Int,
		localized: Localized,
		params: Map<String, Any> = emptyMap(),
		keyboard: Keyboard? = null
	): EditMessageText {

		val effectiveParams = params.ifEmpty { localized.params }
		val effectiveKeyboard = keyboard ?: localized.keyboard

		val edit = EditMessageText.builder()
			.chatId(chatId.toString())
			.messageId(messageId)
			.text(getMessageInternal(localized.localizationCode, effectiveParams))

		return effectiveKeyboard
			?.let { edit.replyMarkup(buildKeyboard(it)).build() }
			?: edit.build()
	}

	fun editMessageText(
		chatId: Long,
		messageId: Int,
		code: LocalizationCode,
		params: Map<String, Any> = emptyMap(),
		keyboard: Keyboard? = null
	): EditMessageText {

		val edit = EditMessageText.builder()
			.chatId(chatId.toString())
			.messageId(messageId)
			.text(getMessageInternal(code, params))

		return keyboard
			?.let { edit.replyMarkup(buildKeyboard(it)).build() }
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
