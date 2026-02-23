package com.sleepkqq.sololeveling.telegram.bot.handler

import com.sleepkqq.sololeveling.telegram.bot.callback.Callback
import com.sleepkqq.sololeveling.telegram.bot.callback.value
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
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
