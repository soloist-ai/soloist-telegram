package com.sleepkqq.sololeveling.telegram.bot.command.info

import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCommand
import com.sleepkqq.sololeveling.telegram.bot.command.Command
import com.sleepkqq.sololeveling.telegram.bot.command.description
import com.sleepkqq.sololeveling.telegram.bot.command.info.InfoCommand.InfoCommandResult
import com.sleepkqq.sololeveling.telegram.bot.command.value
import com.sleepkqq.sololeveling.telegram.bot.service.auth.AuthService
import com.sleepkqq.sololeveling.telegram.bot.service.localization.impl.I18nService
import com.sleepkqq.sololeveling.telegram.localization.CommandCode
import com.sleepkqq.sololeveling.telegram.localization.CommandDescriptionCode
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

