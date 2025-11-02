package com.sleepkqq.sololeveling.telegram.bot.handler

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.message.Message

interface MessageHandler {
	fun handle(message: Message): BotApiMethod<*>?
}
