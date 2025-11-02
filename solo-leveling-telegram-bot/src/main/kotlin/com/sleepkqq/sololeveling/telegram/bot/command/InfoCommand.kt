package com.sleepkqq.sololeveling.telegram.bot.command

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.message.Message

interface InfoCommand : Command {

	fun handle(message: Message): BotApiMethod<*>?
}
