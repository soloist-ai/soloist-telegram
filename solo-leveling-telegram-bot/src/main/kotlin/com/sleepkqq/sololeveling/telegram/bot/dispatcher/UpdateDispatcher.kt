package com.sleepkqq.sololeveling.telegram.bot.dispatcher

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class UpdateDispatcher(
	private val messageDispatcher: MessageDispatcher,
	private val callbackQueryDispatcher: CallbackQueryDispatcher,
	private val inlineQueryDispatcher: InlineQueryDispatcher
) {

	@Transactional
	fun dispatch(update: Update): BotApiMethod<*>? = when {
		update.hasMessage() -> messageDispatcher.dispatch(update.message)
		update.hasCallbackQuery() -> callbackQueryDispatcher.dispatch(update.callbackQuery)
		update.hasInlineQuery() -> inlineQueryDispatcher.dispatch(update.inlineQuery)

		else -> null
	}
}
