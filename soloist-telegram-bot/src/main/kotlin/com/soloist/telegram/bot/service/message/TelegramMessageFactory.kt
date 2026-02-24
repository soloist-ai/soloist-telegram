package com.soloist.telegram.bot.service.message

import com.soloist.telegram.bot.service.image.ImageResourceService
import com.soloist.telegram.bot.service.localization.impl.I18nService
import com.soloist.telegram.bot.service.localization.impl.PhotoSource
import com.soloist.telegram.keyboard.ButtonStyle
import com.soloist.telegram.keyboard.Keyboard
import com.soloist.telegram.localization.LocalizationCode
import com.soloist.telegram.localization.Localized
import com.soloist.telegram.localization.Suggestions
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.LinkPreviewOptions
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.util.Locale

@Service
class TelegramMessageFactory(
	private val i18nService: I18nService,
	private val imageResourceService: ImageResourceService
) {

	private companion object {
		const val HTML_MODE = "HTML"

		val NO_LINK_PREVIEW: LinkPreviewOptions = LinkPreviewOptions.builder()
			.isDisabled(true)
			.build()
	}

	// ============ SendMessage ============

	fun sendMessage(
		chatId: Long,
		localized: Localized,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		suggestions: Suggestions<*>? = null,
		locale: Locale? = null
	): SendMessage = sendMessage(
		chatId = chatId,
		text = i18nService.getMessage(
			localized.localizationCode,
			params.ifEmpty { localized.params },
			locale
		),
		keyboard = keyboard ?: localized.keyboard,
		suggestions = suggestions ?: localized.suggestions,
		locale = locale
	)

	fun sendMessage(
		chatId: Long,
		code: LocalizationCode,
		params: List<Any> = emptyList(),
		keyboard: Keyboard? = null,
		suggestions: Suggestions<*>? = null,
		locale: Locale? = null
	): SendMessage = sendMessage(
		chatId = chatId,
		text = i18nService.getMessage(code, params, locale),
		keyboard = keyboard,
		suggestions = suggestions,
		locale = locale
	)

	fun sendMessage(
		chatId: Long,
		text: String,
		keyboard: Keyboard? = null,
		suggestions: Suggestions<*>? = null,
		locale: Locale? = null
	): SendMessage = SendMessage.builder()
		.chatId(chatId.toString())
		.text(text)
		.parseMode(HTML_MODE)
		.linkPreviewOptions(NO_LINK_PREVIEW)
		.apply {
			when {
				suggestions != null -> replyMarkup(buildSuggestionsKeyboard(suggestions))
				keyboard != null -> replyMarkup(i18nService.buildKeyboard(keyboard, locale = locale))
			}
		}
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
	): EditMessageText = EditMessageText.builder()
		.chatId(chatId.toString())
		.messageId(messageId)
		.text(text)
		.parseMode(HTML_MODE)
		.linkPreviewOptions(NO_LINK_PREVIEW)
		.apply {
			keyboard?.let {
				replyMarkup(i18nService.buildKeyboard(it, locale = locale))
			}
		}
		.build()

	// ============ DeleteMessage ============

	fun deleteMessage(chatId: Long, messageId: Int): DeleteMessage = DeleteMessage.builder()
		.chatId(chatId.toString())
		.messageId(messageId)
		.build()

	// ============ Builders ============

	private fun <T> buildSuggestionsKeyboard(suggestions: Suggestions<T>): ReplyKeyboardMarkup =
		ReplyKeyboardMarkup.builder()
			.keyboard(
				suggestions.items
					.map {
						KeyboardButton.builder()
							.text(suggestions.label(it))
							.style(ButtonStyle.PRIMARY.name.lowercase())
							.build()
					}
					.chunked(suggestions.columns)
					.map { KeyboardRow(it) }
			)
			.resizeKeyboard(true)
			.oneTimeKeyboard(true)
			.selective(true)
			.build()
}