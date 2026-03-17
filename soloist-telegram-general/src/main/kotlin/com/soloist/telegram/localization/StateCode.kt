package com.soloist.telegram.localization

enum class StateCode(override val path: String) : LocalizationCode {
	IDLE("state.idle"),
	TRANSFER_AMOUNT("state.transfer.amount"),
	TRANSFER_RECIPIENT("state.transfer.recipient"),
	TRANSFER_CONFIRMATION("state.transfer.confirmation"),
	FEEDBACK_ENTER("state.feedback.enter"),
	FEEDBACK_EXIT("state.feedback.exit"),
	RESET_PLAYER_ENTER("state.reset.player.enter"),
	RESET_PLAYER_CONFIRMATION("state.reset.player.confirmation"),
	RESET_PLAYER_EXIT("state.reset.player.exit"),
	NEWSLETTER_NAME_ENTER("state.newsletter.name.enter"),
	NEWSLETTER_MESSAGE_ENTER("state.newsletter.message.enter"),
	NEWSLETTER_PHOTO_ENTER("state.newsletter.photo.enter"),
	NEWSLETTER_DATE_TIME_ENTER("state.newsletter.date-time.enter"),
	NEWSLETTER_CONFIRMATION("state.newsletter.confirmation"),
	NEWSLETTER_EXIT("state.newsletter.exit"),
	PROOF_SUBMISSION_TEXT("state.proof.submission.text"),
	PROOF_SUBMISSION_PHOTO("state.proof.submission.photo"),
	PROOF_SUBMISSION_VIDEO("state.proof.submission.video")
}
