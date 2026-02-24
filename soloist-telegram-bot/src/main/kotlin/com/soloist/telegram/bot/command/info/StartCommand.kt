package com.soloist.telegram.bot.command.info

import com.soloist.telegram.bot.annotation.TelegramCommand
import com.soloist.telegram.bot.command.info.InfoCommand.InfoCommandResult
import com.soloist.telegram.keyboard.Keyboard
import com.soloist.telegram.localization.CommandCode
import com.soloist.telegram.localization.CommandDescriptionCode
import org.telegram.telegrambots.meta.api.objects.message.Message

@TelegramCommand("start", CommandDescriptionCode.START)
class StartCommand : InfoCommand {

	override fun handle(message: Message): InfoCommandResult =
		InfoCommandResult(CommandCode.START, keyboard = Keyboard.MINI_APP_LINK)
}
