package com.soloist.telegram.localization

enum class CommandDescriptionCode(override val path: String) : LocalizationCode {
	HELP("command.help.description"),
	START("command.start.description"),
	FEEDBACK("command.feedback.description"),
	USERS_STATS("command.users-stats.description"),
	RESET_PLAYER("command.reset-player.description"),
	SEND_NEWSLETTER("command.send-newsletter.description"),
	TRANSFER("command.transfer.description")
}
