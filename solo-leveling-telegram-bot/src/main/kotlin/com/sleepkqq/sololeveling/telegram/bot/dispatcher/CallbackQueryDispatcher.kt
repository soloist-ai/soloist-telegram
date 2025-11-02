package com.sleepkqq.sololeveling.telegram.bot.dispatcher

import com.sleepkqq.sololeveling.telegram.bot.handler.CallbackQueryHandler
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

@Component
class CallbackQueryDispatcher(
	private val callbackHandlers: List<CallbackQueryHandler>
) {

	fun dispatch(callbackQuery: CallbackQuery): BotApiMethod<*>? = callbackHandlers.asSequence()
		.mapNotNull { it.handle(callbackQuery) }
		.firstOrNull()
}
