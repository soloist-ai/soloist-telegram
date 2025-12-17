package com.sleepkqq.sololeveling.telegram.callback

import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode

enum class CallbackAction(
	val action: String,
	val localizationCode: LocalizationCode
) {

	INTERRUPT_CONFIRM("interrupt_confirm", LocalizationCode.BUTTON_CONFIRM),
	INTERRUPT_CANCEL("interrupt_cancel", LocalizationCode.BUTTON_CANCEL),
	DEPRECATE_ALL_TASKS_CONFIRM("deprecate_all_tasks_confirm", LocalizationCode.BUTTON_CONFIRM),
	DEPRECATE_TASKS_BY_TOPIC_CONFIRM(
		"deprecate_tasks_by_topic_confirm",
		LocalizationCode.BUTTON_CONFIRM
	),
	IDLE_CANCEL("idle_cancel", LocalizationCode.BUTTON_CANCEL),
	RESET_PLAYER_CONFIRM("reset_player_confirm", LocalizationCode.BUTTON_CONFIRM)
}