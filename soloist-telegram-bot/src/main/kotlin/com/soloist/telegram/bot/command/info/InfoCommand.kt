package com.soloist.telegram.bot.command.info

import com.soloist.telegram.bot.command.Command
import com.soloist.telegram.keyboard.Keyboard
import com.soloist.telegram.localization.LocalizationCode
import com.soloist.telegram.localization.Localized
import org.telegram.telegrambots.meta.api.objects.message.Message

interface InfoCommand : Command {

	fun handle(message: Message): InfoCommandResult

	data class InfoCommandResult(
		override val localizationCode: LocalizationCode,
		override val params: List<Any> = emptyList(),
		override val keyboard: Keyboard? = null
	) : Localized
}