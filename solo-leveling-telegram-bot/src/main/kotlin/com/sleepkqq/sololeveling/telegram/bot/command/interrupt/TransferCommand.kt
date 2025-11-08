package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.command.InterruptCommand
import com.sleepkqq.sololeveling.telegram.bot.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.bot.service.localization.I18nService
import com.sleepkqq.sololeveling.telegram.bot.service.session.TelegramUserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.user.TelegramUserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer.WaitingAmountState
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.message.Message

@Profile("player")
@Component
class TransferCommand(
	i18nService: I18nService,
	private val telegramUserSessionService: TelegramUserSessionService
) : InterruptCommand(i18nService) {

	override val command: String = "/transfer"

	override fun changeState(message: Message, session: TelegramUserSession): BotApiMethod<*>? {
		telegramUserSessionService.update(
			Immutables.createTelegramUserSession(session) {
				it.setState(WaitingAmountState())
			}
		)

		return SendMessage(message.chatId.toString(), LocalizationCode.COMMAND_TRANSFER_ANSWER.path)
	}

	override fun pendingInterruptState(message: Message, session: TelegramUserSession) {
		telegramUserSessionService.update(
			Immutables.createTelegramUserSession(session) {
				it.setPendingInterruptState(WaitingAmountState())
			}
		)
	}
}