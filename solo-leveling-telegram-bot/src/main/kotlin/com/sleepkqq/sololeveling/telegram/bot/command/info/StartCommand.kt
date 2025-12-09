package com.sleepkqq.sololeveling.telegram.bot.command.info

import com.sleepkqq.sololeveling.telegram.bot.command.info.InfoCommand.InfoCommandResult
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class StartCommand : InfoCommand {

	override val command: String = "/start"

	override fun handle(message: Message): InfoCommandResult =
		InfoCommandResult(LocalizationCode.CMD_START)
}
