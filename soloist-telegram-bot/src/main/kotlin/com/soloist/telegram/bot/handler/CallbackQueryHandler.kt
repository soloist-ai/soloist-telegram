package com.soloist.telegram.bot.handler

import com.soloist.telegram.bot.callback.Callback
import com.soloist.telegram.bot.callback.value
import com.soloist.telegram.bot.service.user.UserSessionService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

@Component
class CallbackQueryHandler(
	callbacks: List<Callback>,
	private val userSessionService: UserSessionService
) {

	private val callbacksMap = callbacks.associateBy { it.value().data }

	fun handle(callbackQuery: CallbackQuery): BotApiMethod<*>? {
		val userId = callbackQuery.from.id
		val session = userSessionService.find(userId)
			?: return null

		return callbacksMap[callbackQuery.data]
			?.handle(callbackQuery, session)
	}
}
