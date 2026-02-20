package com.sleepkqq.sololeveling.telegram.bot.handler.impl

import com.sleepkqq.sololeveling.telegram.bot.handler.MessageHandler
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.bot.state.StateProcessor
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.model.entity.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.reflect.KClass

@Component
class StateMessageHandler(
	private val userSessionService: UserSessionService,
	private val telegramMessageFactory: TelegramMessageFactory,
	stateProcessors: List<StateProcessor<out BotSessionState>>
) : MessageHandler {

	private val stateProcessorsMap: Map<KClass<out BotSessionState>, StateProcessor<BotSessionState>> =
		stateProcessors.associate { processor ->
			@Suppress("UNCHECKED_CAST")
			(processor as StateProcessor<BotSessionState>)
				.let { it.getStateClass() to it }
		}

	override fun handle(message: Message): BotApiMethod<*>? {
		val session = userSessionService.find(message.chatId)
			?: return telegramMessageFactory.sendMessage(message.chatId, LocalizationCode.STATE_IDLE)

		val currentState = session.state()

		// Эти процессоры нужны в случае если у стейта в конечном итоге нет кнопок подтверждения
		val processed = stateProcessorsMap[currentState::class]
			?.process(message, currentState)
			?: true

		if (!processed) {
			return telegramMessageFactory.sendMessage(message.chatId, currentState.onEnterLocalized())
		}

		val newState = currentState.nextState(message)

		if (currentState != newState) {
			userSessionService.update(
				Immutables.createUserSession(session) {
					it.setState(newState)
						.setPendingInterruptState(null)
				}
			)
		}

		if (currentState.onExitMessageCode() != null) {
			return telegramMessageFactory.sendMessage(message.chatId, currentState.onExitLocalized()!!)
		}

		return telegramMessageFactory.sendMessage(message.chatId, newState.onEnterLocalized())
	}
}
