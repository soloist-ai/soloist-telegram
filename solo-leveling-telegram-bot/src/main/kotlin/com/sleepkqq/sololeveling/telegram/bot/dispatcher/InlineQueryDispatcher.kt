package com.sleepkqq.sololeveling.telegram.bot.dispatcher

import com.sleepkqq.sololeveling.telegram.bot.handler.InlineQueryHandler
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery

@Component
class InlineQueryDispatcher(
	private val handlers: List<InlineQueryHandler>
) {

	fun dispatch(inlineQuery: InlineQuery): BotApiMethod<*>? = handlers.asSequence()
		.mapNotNull { it.handle(inlineQuery) }
		.firstOrNull()
}
