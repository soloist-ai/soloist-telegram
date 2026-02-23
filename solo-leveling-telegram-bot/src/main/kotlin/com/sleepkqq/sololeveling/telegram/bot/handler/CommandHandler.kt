package com.sleepkqq.sololeveling.telegram.bot.handler

import com.sleepkqq.sololeveling.telegram.bot.command.Command
import com.sleepkqq.sololeveling.telegram.bot.command.info.InfoCommand
import com.sleepkqq.sololeveling.telegram.bot.command.interrupt.InterruptCommand
import com.sleepkqq.sololeveling.telegram.bot.command.value
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.extensions.command
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.localization.CommandCode
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class CommandHandler(
	commands: List<Command>,
	private val userSessionService: UserSessionService,
	private val telegramMessageFactory: TelegramMessageFactory
) {

	private val commandsMap: Map<String, Command> = commands.associateBy { it.value() }

	fun handle(message: Message): BotApiMethod<*>? {
		val command = message.command()?.let { commandsMap[it] }
			?: return telegramMessageFactory.sendMessage(message.chatId, CommandCode.UNKNOWN)

		return when (command) {
			is InfoCommand -> {
				val result = command.handle(message)
				telegramMessageFactory.sendMessage(message.chatId, result)
			}

			is InterruptCommand -> {
				val session = userSessionService.find(message.chatId)
					?: userSessionService.register(message.chatId)

				command.handle(message, session)
					?.let { telegramMessageFactory.sendMessage(message.chatId, it.localized) }
					?: return null
			}

			else -> null
		}
	}
}
