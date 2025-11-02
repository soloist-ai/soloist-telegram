package com.sleepkqq.sololeveling.telegram.bot.handler.impl

import com.sleepkqq.sololeveling.telegram.bot.handler.MessageHandler
import com.sleepkqq.sololeveling.telegram.bot.service.TelegramUserSessionService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class TextHandler(
	private val telegramUserSessionService: TelegramUserSessionService
) : MessageHandler {

	override fun handle(message: Message): BotApiMethod<*>? =
		telegramUserSessionService.find(message.chatId)
			?.let { SendMessage(message.chatId.toString(), it.state().message()) }
			?: SendMessage(message.chatId.toString(), "Привет! Для начала работы бота введи /start")
}
