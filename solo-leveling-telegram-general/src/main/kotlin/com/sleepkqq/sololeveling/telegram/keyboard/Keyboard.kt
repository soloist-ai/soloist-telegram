package com.sleepkqq.sololeveling.telegram.keyboard

import com.sleepkqq.sololeveling.telegram.callback.CallbackAction

enum class Keyboard(
	val actions: List<CallbackAction>
) {

	INTERRUPT_CONFIRMATION(
		listOf(
			CallbackAction.INTERRUPT_CONFIRM,
			CallbackAction.INTERRUPT_CANCEL
		)
	),
	DEPRECATE_ALL_TASKS_CONFIRMATION(
		listOf(
			CallbackAction.DEPRECATE_ALL_TASKS_CONFIRM,
			CallbackAction.DEPRECATE_ALL_TASKS_CANCEL
		)
	),
	DEPRECATE_TASKS_BY_TOPIC_CONFIRMATION(
		listOf(
			CallbackAction.DEPRECATE_TASKS_BY_TOPIC_CONFIRM,
			CallbackAction.DEPRECATE_TASKS_BY_TOPIC_CANCEL
		)
	)
}
