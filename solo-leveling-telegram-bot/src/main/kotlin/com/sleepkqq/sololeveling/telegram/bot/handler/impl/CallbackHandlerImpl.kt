package com.sleepkqq.sololeveling.telegram.bot.handler.impl

import com.sleepkqq.sololeveling.telegram.bot.callback.Callback
import com.sleepkqq.sololeveling.telegram.bot.handler.CallbackQueryHandler
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

@Component
class CallbackHandlerImpl(
	callbacks: List<Callback>,
	private val userSessionService: UserSessionService
) : CallbackQueryHandler {

	private val callbacksMap = callbacks.associateBy { it.action.action }

	override fun handle(callbackQuery: CallbackQuery): BotApiMethod<*>? {
		val userId = callbackQuery.from.id
		val session = userSessionService.find(userId)
			?: return null

		return callbacksMap[callbackQuery.data]
			?.handle(callbackQuery, session)
	}
}
