package com.soloist.telegram.bot.command.info

import com.soloist.telegram.bot.annotation.TelegramCommand
import com.soloist.telegram.bot.command.Command
import com.soloist.telegram.bot.command.description
import com.soloist.telegram.bot.command.info.InfoCommand.InfoCommandResult
import com.soloist.telegram.bot.command.value
import com.soloist.telegram.bot.service.auth.AuthService
import com.soloist.telegram.bot.service.localization.impl.I18nService
import com.soloist.telegram.localization.CommandCode
import com.soloist.telegram.localization.CommandDescriptionCode
import org.springframework.beans.factory.ObjectProvider
import org.telegram.telegrambots.meta.api.objects.message.Message

@TelegramCommand("help", CommandDescriptionCode.HELP)
class HelpCommand(
	private val commandProvider: ObjectProvider<Command>,
	private val i18nService: I18nService,
	private val authService: AuthService
) : InfoCommand {

	private val commands: List<Command> by lazy {
		commandProvider.sortedBy { it.value() }
	}

	override fun handle(message: Message): InfoCommandResult {
		val helpText = commands.filter(authService::hasAccess)
			.joinToString("\n") {
				"/${it.value()} - ${i18nService.getMessage(it.description())}"
			}

		return InfoCommandResult(CommandCode.HELP, listOf(helpText))
	}
}

