package com.sleepkqq.sololeveling.telegram.bot.dispatcher

import com.sleepkqq.sololeveling.telegram.bot.handler.impl.CommandHandler
import com.sleepkqq.sololeveling.telegram.bot.handler.impl.TextHandler
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class MessageDispatcher(
	private val commandHandler: CommandHandler,
	private val textHandler: TextHandler
) {

	fun dispatch(message: Message): BotApiMethod<*>? = when {
		message.isCommand -> commandHandler.handle(message)
		message.hasText() -> textHandler.handle(message)

		else -> null
	}
}
