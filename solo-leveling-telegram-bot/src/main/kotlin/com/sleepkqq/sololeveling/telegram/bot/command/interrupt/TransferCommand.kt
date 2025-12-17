package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer.TransferAmountState
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.message.Message

@Profile("transfer")
@Component
class TransferCommand(
	override val userSessionService: UserSessionService
) : InterruptCommand<TransferAmountState> {

	override val command: String = "/transfer"
	override val forList: Boolean = false

	override fun createState(message: Message, session: UserSession): TransferAmountState =
		TransferAmountState()
}
