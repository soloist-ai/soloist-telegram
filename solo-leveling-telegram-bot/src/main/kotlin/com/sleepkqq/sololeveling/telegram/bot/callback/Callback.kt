package com.sleepkqq.sololeveling.telegram.bot.callback

import com.sleepkqq.sololeveling.telegram.bot.service.auth.RoleProtected
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

interface Callback : RoleProtected {

	val action: CallbackAction

	fun handle(callbackQuery: CallbackQuery, session: UserSession): BotApiMethod<*>?
}
