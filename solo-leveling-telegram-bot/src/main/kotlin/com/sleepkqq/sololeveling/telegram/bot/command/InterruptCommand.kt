package com.sleepkqq.sololeveling.telegram.bot.command

import com.sleepkqq.sololeveling.telegram.model.entity.user.TelegramUserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.IdleState
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.message.Message

interface InterruptCommand : Command {

	fun handle(message: Message, session: TelegramUserSession): BotApiMethod<*>? {
		if (session.state() is IdleState) {
			return changeState(message, session)
		} else {
			pendingInterruptState(message, session)
			return SendMessage(
				message.chatId.toString(),
				"Вы уверены что хотите прервать текущее заполнение?"
			)
		}
	}

	fun changeState(message: Message, session: TelegramUserSession): BotApiMethod<*>?

	fun pendingInterruptState(message: Message, session: TelegramUserSession)
}
