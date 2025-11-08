package com.sleepkqq.sololeveling.telegram.bot.command

import com.sleepkqq.sololeveling.telegram.bot.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.bot.service.localization.I18nService
import com.sleepkqq.sololeveling.telegram.model.entity.user.TelegramUserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.IdleState
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.message.Message

abstract class InterruptCommand(
	private val i18nService: I18nService
) : Command {

	fun handle(message: Message, session: TelegramUserSession): BotApiMethod<*>? =
		if (session.state() is IdleState) {
			changeState(message, session)
		} else {
			pendingInterruptState(message, session)
			SendMessage(
				message.chatId.toString(),
				i18nService.getMessage(LocalizationCode.COMMAND_INTERRUPT_QUESTION.path)
			)
		}

	abstract fun changeState(message: Message, session: TelegramUserSession): BotApiMethod<*>?

	abstract fun pendingInterruptState(message: Message, session: TelegramUserSession)
}
