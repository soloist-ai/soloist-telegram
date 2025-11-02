package com.sleepkqq.sololeveling.telegram.bot.command.info

import com.sleepkqq.sololeveling.telegram.bot.command.InfoCommand
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class HelpCommand : InfoCommand {

	override val command: String = "/help"

	override fun handle(message: Message): BotApiMethod<*>? =
		SendMessage(message.chatId.toString(), "помощь")
}
