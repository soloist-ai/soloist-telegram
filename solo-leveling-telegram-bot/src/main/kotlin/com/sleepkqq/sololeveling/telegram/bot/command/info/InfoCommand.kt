package com.sleepkqq.sololeveling.telegram.bot.command.info

import com.sleepkqq.sololeveling.telegram.bot.command.Command
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import org.telegram.telegrambots.meta.api.objects.message.Message

interface InfoCommand : Command {

	fun handle(message: Message): InfoCommandResult

	data class InfoCommandResult(
		val localizationCode: LocalizationCode,
		val params: Map<String, Any> = emptyMap()
	) {
		constructor(code: LocalizationCode, vararg pairs: Pair<String, Any>) :
				this(code, mapOf(*pairs))
	}
}