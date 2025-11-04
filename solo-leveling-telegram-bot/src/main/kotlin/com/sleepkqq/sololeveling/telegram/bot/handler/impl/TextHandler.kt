package com.sleepkqq.sololeveling.telegram.bot.handler.impl

import com.sleepkqq.sololeveling.telegram.bot.handler.MessageHandler
import com.sleepkqq.sololeveling.telegram.bot.service.TelegramUserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.Immutables
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class TextHandler(
	private val telegramUserSessionService: TelegramUserSessionService
) : MessageHandler {

	override fun handle(message: Message): BotApiMethod<*>? {
		val session = telegramUserSessionService.find(message.chatId)
			?: return SendMessage(
				message.chatId.toString(),
				"Привет! Для начала работы бота введи /start"
			)

		val currentState = session.state()
		val newState = currentState.nextState(message.text)

		if (currentState != newState) {
			telegramUserSessionService.update(
				Immutables.createTelegramUserSession(session) {
					it.setState(newState)
				}
			)
		}

		return SendMessage(message.chatId.toString(), newState.message())
	}
}
