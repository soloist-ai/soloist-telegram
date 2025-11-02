package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.command.InterruptCommand
import com.sleepkqq.sololeveling.telegram.bot.service.TelegramUserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.user.TelegramUserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.TransferFlow
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class TransferCommand(
	private val telegramUserSessionService: TelegramUserSessionService
) : InterruptCommand {

	override val command: String = "/transfer"

	override fun changeState(message: Message, session: TelegramUserSession): BotApiMethod<*>? {
		telegramUserSessionService.update(
			Immutables.createTelegramUserSession(session) {
				it.setState(TransferFlow.WaitingAmount())
			}
		)
		return SendMessage(message.chatId.toString(), "Введите сумму перевода")
	}

	override fun pendingInterruptState(message: Message, session: TelegramUserSession) {
		telegramUserSessionService.update(
			Immutables.createTelegramUserSession(session) {
				it.setPendingInterruptState(TransferFlow.WaitingAmount())
			}
		)
	}
}