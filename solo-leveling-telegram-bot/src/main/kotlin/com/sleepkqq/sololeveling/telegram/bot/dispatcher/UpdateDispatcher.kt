package com.sleepkqq.sololeveling.telegram.bot.dispatcher

import com.sleepkqq.sololeveling.telegram.bot.handler.CallbackQueryHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class UpdateDispatcher(
	private val messageDispatcher: MessageDispatcher,
	private val callbackQueryHandler: CallbackQueryHandler
) {

	@Transactional
	fun dispatch(update: Update): BotApiMethod<*>? = when {
		update.hasMessage() -> messageDispatcher.dispatch(update.message)
		update.hasCallbackQuery() -> callbackQueryHandler.handle(update.callbackQuery)

		else -> null
	}
}
