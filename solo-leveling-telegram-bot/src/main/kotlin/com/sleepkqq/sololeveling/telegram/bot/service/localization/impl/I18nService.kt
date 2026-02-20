package com.sleepkqq.sololeveling.telegram.bot.service.localization.impl

import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard
import com.sleepkqq.sololeveling.telegram.keyboard.KeyboardAction
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
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
