package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.command.Command
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.localization.Localized
import com.sleepkqq.sololeveling.telegram.model.entity.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.IdleState
import org.telegram.telegrambots.meta.api.objects.message.Message

interface InterruptCommand<S : BotSessionState> : Command {

	val userSessionService: UserSessionService

	fun createState(message: Message, session: UserSession): S?

	fun handle(message: Message, session: UserSession): InterruptCommandResult<S>? =
		if (session.state() is IdleState) {
			changeState(message, session)
		} else {
			pendingInterruptState(message, session)
		}

	fun changeState(
		message: Message,
		session: UserSession
	): InterruptCommandResult.StateChanged<S>? {
		val newState = createState(message, session)
			?: return null

		userSessionService.update(
			Immutables.createUserSession(session) {
				it.setState(newState)
			}
		)

		return InterruptCommandResult.StateChanged(newState)
	}

	fun pendingInterruptState(
		message: Message,
		session: UserSession
	): InterruptCommandResult.Question? {
		val pendingState = createState(message, session)
			?: return null

		userSessionService.update(
			Immutables.createUserSession(session) {
				it.setPendingInterruptState(pendingState)
			}
		)

		return InterruptCommandResult.Question()
	}

	sealed class InterruptCommandResult<out S : BotSessionState> : Localized {

		data class Question(
			override val localizationCode: LocalizationCode = LocalizationCode.CMD_INTERRUPT,
			override val keyboard: Keyboard = Keyboard.INTERRUPT_CONFIRMATION
		) : InterruptCommandResult<Nothing>()

		data class StateChanged<S : BotSessionState>(
			private val botSessionState: S
		) : InterruptCommandResult<S>() {
			override val localizationCode: LocalizationCode
				get() = botSessionState.onEnterMessageCode()

			override val params: List<Any>
				get() = botSessionState.onEnterMessageParams()

			override val keyboard: Keyboard?
				get() = botSessionState.onEnterMessageKeyboard()
		}
	}
}