package com.soloist.telegram.localization

enum class CommandCode(override val path: String) : LocalizationCode {
	HELP("command.help"),
	START("command.start"),
	USERS_STATS("command.users-stats"),
	UNKNOWN("command.unknown"),
	INTERRUPT("command.interrupt"),
}
