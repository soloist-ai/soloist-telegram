package com.soloist.telegram.bot.service.localization.impl

import com.soloist.telegram.keyboard.Keyboard
import com.soloist.telegram.keyboard.KeyboardData
import com.soloist.telegram.localization.LocalizationCode
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow
import java.util.*

@Service
class I18nService(
	private val messageSource: MessageSource,
	private val environment: Environment,
) {

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
		buttonsPerRow: Int = keyboard.data.size,
		locale: Locale? = null
	): InlineKeyboardMarkup {
		val buttons = keyboard.data.map { createButton(it, locale) }
		val rows = buttons.chunked(buttonsPerRow).map { InlineKeyboardRow(it) }
		return InlineKeyboardMarkup(rows)
	}

	private fun createButton(action: KeyboardData, locale: Locale? = null): InlineKeyboardButton {
		val button = action.button
		return InlineKeyboardButton.builder()
			.text(getMessage(button.localizationCode, locale = locale))
			.apply {
				when (action) {
					is KeyboardData.Callback -> callbackData(action.callbackData.data)
					is KeyboardData.Url -> url(environment.getRequiredProperty(action.urlProperty))
				}
				button.customEmojiId?.let { iconCustomEmojiId(it) }
				button.style?.let { style(it.name.lowercase()) }
			}
			.build()
	}
}
