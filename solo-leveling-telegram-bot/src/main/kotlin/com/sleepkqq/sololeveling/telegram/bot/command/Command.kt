package com.sleepkqq.sololeveling.telegram.bot.command

interface Command {

	val command: String

	val visible: Boolean

	val description: String
		get() = ""
}
