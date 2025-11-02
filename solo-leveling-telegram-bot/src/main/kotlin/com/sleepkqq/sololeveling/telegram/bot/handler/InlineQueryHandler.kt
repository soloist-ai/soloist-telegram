package com.sleepkqq.sololeveling.telegram.bot.handler

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery

interface InlineQueryHandler {
	fun handle(inlineQuery: InlineQuery): BotApiMethod<*>?
}
