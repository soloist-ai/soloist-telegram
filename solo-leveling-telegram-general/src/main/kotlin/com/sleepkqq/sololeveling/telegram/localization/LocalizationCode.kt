package com.sleepkqq.sololeveling.telegram.localization

enum class LocalizationCode(val path: String) {

	// ========== Commands ==========
	CMD_HELP("command.help"),
	CMD_START("command.start"),
	CMD_UNKNOWN("command.unknown"),
	CMD_INTERRUPT("command.interrupt"),

	// ========== States ==========
	STATE_IDLE("state.idle"),
	STATE_TRANSFER_AMOUNT("state.transfer.amount"),
	STATE_TRANSFER_RECIPIENT("state.transfer.recipient"),
	STATE_TRANSFER_CONFIRMATION("state.transfer.confirmation"),
	STATE_FEEDBACK_ENTER("state.feedback.enter"),
	STATE_FEEDBACK_EXIT("state.feedback.exit"),
	STATE_TASKS_DEPRECATE_ALL_CONFIRMATION("state.tasks.deprecate-all.confirmation"),
	STATE_TASKS_DEPRECATE_BY_TOPIC_ENTER("state.tasks.deprecate-by-topic.enter"),
	STATE_TASKS_DEPRECATE_BY_TOPIC_CONFIRMATION("state.tasks.deprecate-by-topic.confirmation"),
	STATE_TASKS_DEPRECATE_EXIT("state.tasks.deprecate.exit"),

	// ========== Buttons ==========
	BUTTON_CONFIRM("button.confirm"),
	BUTTON_CANCEL("button.cancel"),
	BUTTON_MINI_APP_LINK("button.mini-app.link"),

	// ========== Errors ===========
	ERROR_ACCESS_DENIED("error.access-denied"),

	// ========== Info ===========
	INFO_ACTION_CANCELED("info.action.canceled"),
}
