package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.callback.Callback
import com.sleepkqq.sololeveling.telegram.bot.service.localization.I18nService
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

@Component
class IdleCancelCallback(
	private val userSessionService: UserSessionService,
	private val i18nService: I18nService
) : Callback {

	override val action: CallbackAction = CallbackAction.IDLE_CANCEL

	override fun handle(callbackQuery: CallbackQuery, session: UserSession): BotApiMethod<*> {
		val userId = callbackQuery.from.id
		val messageId = callbackQuery.message.messageId

		userSessionService.idleState(userId)

		return i18nService.editMessageText(
			userId,
			messageId,
			LocalizationCode.INFO_ACTION_CANCELED
		)
	}
}
