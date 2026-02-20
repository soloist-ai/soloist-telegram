package com.sleepkqq.sololeveling.telegram.bot.handler.impl

import com.sleepkqq.sololeveling.telegram.bot.command.Command
import com.sleepkqq.sololeveling.telegram.bot.command.info.InfoCommand
import com.sleepkqq.sololeveling.telegram.bot.command.interrupt.InterruptCommand
import com.sleepkqq.sololeveling.telegram.bot.handler.MessageHandler
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class CommandHandler(
	commands: List<Command>,
	private val userSessionService: UserSessionService,
	private val telegramMessageFactory: TelegramMessageFactory
) : MessageHandler {

	private val commandsMap: Map<String, Command> = commands.associateBy { it.command }

	override fun handle(message: Message): BotApiMethod<*>? {
		val commandText = message.text.split(" ").first()
		val command = commandsMap[commandText]
			?: return telegramMessageFactory.sendMessage(message.chatId, LocalizationCode.CMD_UNKNOWN)

		return when (command) {
			is InfoCommand -> {
				val result = command.handle(message)
				telegramMessageFactory.sendMessage(message.chatId, result)
			}

			is InterruptCommand<*> -> {
				val session = userSessionService.find(message.chatId)
					?: userSessionService.register(message.chatId)

				command.handle(message, session)
					?.let { telegramMessageFactory.sendMessage(message.chatId, it) }
					?: return null
			}

			else -> null
		}
	}
}
