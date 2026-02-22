package com.sleepkqq.sololeveling.telegram.bot.dispatcher

import com.sleepkqq.sololeveling.telegram.bot.handler.CommandHandler
import com.sleepkqq.sololeveling.telegram.bot.handler.StateMessageHandler
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class MessageDispatcher(
	private val commandHandler: CommandHandler,
	private val stateMessageHandler: StateMessageHandler
) {

	fun dispatch(message: Message): BotApiMethod<*>? = when {
		message.isCommand -> commandHandler.handle(message)
		else -> stateMessageHandler.handle(message)
	}
}
