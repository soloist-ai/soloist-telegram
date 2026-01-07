package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.callback.Callback
import com.sleepkqq.sololeveling.telegram.bot.grpc.client.PlayerApi
import com.sleepkqq.sololeveling.telegram.bot.service.localization.I18nService
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

@Component
class DeprecateAllTasksConfirmCallback(
	private val userSessionService: UserSessionService,
	private val playerApi: PlayerApi,
	private val i18nService: I18nService
) : Callback {

	override val action: CallbackAction = CallbackAction.DEPRECATE_ALL_TASKS_CONFIRM

	@PreAuthorize("hasAuthority('DEVELOPER')")
	override fun handle(callbackQuery: CallbackQuery, session: UserSession): BotApiMethod<*> {
		val userId = callbackQuery.from.id
		val messageId = callbackQuery.message.messageId

		userSessionService.idleState(userId)

		val effectedRows = playerApi.deprecateAllTasks()

		return i18nService.editMessageText(
			userId,
			messageId,
			session.state().onExitLocalized()!!,
			listOf(effectedRows),
		)
	}
}
