package com.sleepkqq.sololeveling.telegram.callback

import com.sleepkqq.sololeveling.telegram.keyboard.Button

enum class CallbackData(
	val data: String,
	val button: Button
) {

	INTERRUPT_CONFIRM("interrupt_confirm", Button.CONFIRM),
	INTERRUPT_CANCEL("interrupt_cancel", Button.CANCEL),
	DEPRECATE_ALL_TASKS_CONFIRM("deprecate_all_tasks_confirm", Button.CONFIRM),
	DEPRECATE_TASKS_BY_TOPIC_CONFIRM("deprecate_tasks_by_topic_confirm", Button.CONFIRM),
	IDLE_CANCEL("idle_cancel", Button.CANCEL),
	RESET_PLAYER_CONFIRM("reset_player_confirm", Button.CONFIRM),
	SEND_NEWSLETTER_CONFIRM("send_newsletter_confirm", Button.CONFIRM)
}
