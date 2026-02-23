package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCallback
import com.sleepkqq.sololeveling.telegram.bot.callback.Callback
import com.sleepkqq.sololeveling.telegram.bot.grpc.client.PlayerApi
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.callback.CallbackData
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

@TelegramCallback(CallbackData.DEPRECATE_ALL_TASKS_CONFIRM)
class DeprecateAllTasksConfirmCallback(
	private val userSessionService: UserSessionService,
	private val playerApi: PlayerApi,
	private val telegramMessageFactory: TelegramMessageFactory
) : Callback {

	override fun handle(callbackQuery: CallbackQuery, session: UserSession): BotApiMethod<*> {
		val userId = callbackQuery.from.id
		val messageId = callbackQuery.message.messageId

		userSessionService.idleState(userId)

		val affectedRows = playerApi.deprecateAllTasks()

		return telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = session.state().onExitLocalized()!!,
			params = listOf(affectedRows),
		)
	}
}
