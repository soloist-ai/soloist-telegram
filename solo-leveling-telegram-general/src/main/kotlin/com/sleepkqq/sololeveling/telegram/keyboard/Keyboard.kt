package com.sleepkqq.sololeveling.telegram.keyboard

import com.sleepkqq.sololeveling.telegram.keyboard.KeyboardAction.Callback
import com.sleepkqq.sololeveling.telegram.keyboard.KeyboardAction.Url
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode

enum class Keyboard(
	val actions: List<KeyboardAction>
) {

	INTERRUPT_CONFIRMATION(
		listOf(
			Callback(CallbackAction.INTERRUPT_CONFIRM),
			Callback(CallbackAction.INTERRUPT_CANCEL)
		)
	),
	DEPRECATE_ALL_TASKS_CONFIRMATION(
		listOf(
			Callback(CallbackAction.DEPRECATE_ALL_TASKS_CONFIRM),
			Callback(CallbackAction.DEPRECATE_TASKS_CANCEL)
		)
	),
	DEPRECATE_TASKS_BY_TOPIC_CONFIRMATION(
		listOf(
			Callback(CallbackAction.DEPRECATE_TASKS_BY_TOPIC_CONFIRM),
			Callback(CallbackAction.DEPRECATE_TASKS_CANCEL)
		)
	),
	MINI_APP_LINK(
		listOf(
			Url(LocalizationCode.BUTTON_MINI_APP_LINK, "app.telegram.bot.mini-app.url")
		)
	)
}
