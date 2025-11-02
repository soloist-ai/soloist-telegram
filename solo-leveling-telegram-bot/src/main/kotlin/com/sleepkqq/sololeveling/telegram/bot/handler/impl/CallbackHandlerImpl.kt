package com.sleepkqq.sololeveling.telegram.bot.handler.impl

import com.sleepkqq.sololeveling.telegram.bot.handler.CallbackQueryHandler
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

@Component
class CallbackHandlerImpl : CallbackQueryHandler {
	override fun handle(callbackQuery: CallbackQuery): BotApiMethod<*>? =
		SendMessage(callbackQuery.message.chatId.toString(), "Вы нажали: ${callbackQuery.data}")
}
