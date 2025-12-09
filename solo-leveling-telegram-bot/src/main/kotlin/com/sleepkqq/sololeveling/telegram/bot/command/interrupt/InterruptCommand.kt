package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.command.Command
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.IdleState
import org.telegram.telegrambots.meta.api.objects.message.Message

interface InterruptCommand : Command {

	fun handle(message: Message, session: UserSession): InterruptCommandResult =
		if (session.state() is IdleState) {
			changeState(message, session)
		} else {
			pendingInterruptState(message, session)
			InterruptCommandResult.Question()
		}

	fun changeState(
		message: Message,
		session: UserSession
	): InterruptCommandResult.StateChanged

	fun pendingInterruptState(message: Message, session: UserSession)

	sealed class InterruptCommandResult {

		data class Question(
			val localizationCode: LocalizationCode = LocalizationCode.CMD_INTERRUPT
		) : InterruptCommandResult()

		data class StateChanged(
			val localizationCode: LocalizationCode,
			val params: Map<String, Any> = emptyMap()
		) : InterruptCommandResult()
	}
}