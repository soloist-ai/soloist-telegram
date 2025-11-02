package com.sleepkqq.sololeveling.telegram.bot.handler

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

interface CallbackQueryHandler {
	fun handle(callbackQuery: CallbackQuery): BotApiMethod<*>?
}
