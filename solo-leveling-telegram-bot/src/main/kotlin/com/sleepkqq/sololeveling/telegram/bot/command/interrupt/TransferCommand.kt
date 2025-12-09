package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.command.interrupt.InterruptCommand.InterruptCommandResult.StateChanged
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.model.entity.user.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer.TransferAmountState
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.message.Message

@Profile("transfer")
@Component
class TransferCommand(
	private val userSessionService: UserSessionService
) : InterruptCommand {

	override val command: String = "/transfer"

	override fun changeState(
		message: Message,
		session: UserSession
	): StateChanged {

		userSessionService.update(
			Immutables.createUserSession(session) {
				it.setState(TransferAmountState())
			}
		)

		return StateChanged(LocalizationCode.CMD_TRANSFER)
	}

	override fun pendingInterruptState(message: Message, session: UserSession) {
		userSessionService.update(
			Immutables.createUserSession(session) {
				it.setPendingInterruptState(TransferAmountState())
			}
		)
	}
}
