package com.soloist.telegram.localization

enum class StateCode(override val path: String) : LocalizationCode {
	IDLE("state.idle"),
	TRANSFER_AMOUNT("state.transfer.amount"),
	TRANSFER_RECIPIENT("state.transfer.recipient"),
	TRANSFER_CONFIRMATION("state.transfer.confirmation"),
	FEEDBACK_ENTER("state.feedback.enter"),
	FEEDBACK_EXIT("state.feedback.exit"),
	TASKS_DEPRECATE_ALL_CONFIRMATION("state.tasks.deprecate-all.confirmation"),
	TASKS_DEPRECATE_BY_TOPIC_ENTER("state.tasks.deprecate-by-topic.enter"),
	TASKS_DEPRECATE_BY_TOPIC_CONFIRMATION("state.tasks.deprecate-by-topic.confirmation"),
	TASKS_DEPRECATE_EXIT("state.tasks.deprecate.exit"),
	RESET_PLAYER_ENTER("state.reset.player.enter"),
	RESET_PLAYER_CONFIRMATION("state.reset.player.confirmation"),
	RESET_PLAYER_EXIT("state.reset.player.exit"),
	NEWSLETTER_NAME_ENTER("state.newsletter.name.enter"),
	NEWSLETTER_MESSAGE_ENTER("state.newsletter.message.enter"),
	NEWSLETTER_PHOTO_ENTER("state.newsletter.photo.enter"),
	NEWSLETTER_DATE_TIME_ENTER("state.newsletter.date-time.enter"),
	NEWSLETTER_CONFIRMATION("state.newsletter.confirmation"),
	NEWSLETTER_EXIT("state.newsletter.exit")
}
