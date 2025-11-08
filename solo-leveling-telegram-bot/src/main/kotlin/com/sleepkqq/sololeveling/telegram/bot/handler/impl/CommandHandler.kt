package com.sleepkqq.sololeveling.telegram.bot.handler.impl

import com.sleepkqq.sololeveling.telegram.bot.command.Command
import com.sleepkqq.sololeveling.telegram.bot.command.InfoCommand
import com.sleepkqq.sololeveling.telegram.bot.command.InterruptCommand
import com.sleepkqq.sololeveling.telegram.bot.handler.MessageHandler
import com.sleepkqq.sololeveling.telegram.bot.service.session.TelegramUserSessionService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class CommandHandler(
	commands: List<Command>,
	private val telegramUserSessionService: TelegramUserSessionService
) : MessageHandler {

	private val commandsMap: Map<String, Command> = commands.associateBy { it.command }

	override fun handle(message: Message): BotApiMethod<*>? {
		val commandText = message.text.split(" ").first()
		val command = commandsMap[commandText]
			?: return SendMessage(message.chatId.toString(), "Неизвестная команда")

		return when (command) {
			is InfoCommand -> command.handle(message)

			is InterruptCommand -> {
				val session = telegramUserSessionService.find(message.chatId)
					?: telegramUserSessionService.register(message.chatId)

				command.handle(message, session)
			}
		}
	}
}
