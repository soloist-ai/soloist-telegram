package com.sleepkqq.sololeveling.telegram.bot.command.info

import com.sleepkqq.sololeveling.telegram.bot.command.Command
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.localization.Localized
import org.telegram.telegrambots.meta.api.objects.message.Message

interface InfoCommand : Command {

	fun handle(message: Message): InfoCommandResult

	data class InfoCommandResult(
		override val localizationCode: LocalizationCode,
		override val params: List<Any> = emptyList(),
		override val keyboard: Keyboard? = null
	) : Localized
}