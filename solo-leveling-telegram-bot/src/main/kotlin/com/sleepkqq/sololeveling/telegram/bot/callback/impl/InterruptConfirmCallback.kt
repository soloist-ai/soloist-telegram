package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.callback.Callback
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

@Component
class InterruptConfirmCallback(
	private val userSessionService: UserSessionService,
	private val telegramMessageFactory: TelegramMessageFactory
) : Callback {

	override val action: CallbackAction = CallbackAction.INTERRUPT_CONFIRM

	override fun handle(callbackQuery: CallbackQuery, session: UserSession): BotApiMethod<*> {
		val userId = callbackQuery.from.id
		val messageId = callbackQuery.message.messageId

		val pendingInterruptState = session.pendingInterruptState()
			?: return telegramMessageFactory.deleteMessage(userId, messageId)

		userSessionService.confirmInterruptState(userId)

		return telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = pendingInterruptState.onEnterLocalized()
		)
	}
}
