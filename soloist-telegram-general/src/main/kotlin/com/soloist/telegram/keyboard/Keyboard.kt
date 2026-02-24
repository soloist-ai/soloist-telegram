package com.soloist.telegram.keyboard

import com.soloist.telegram.keyboard.KeyboardData.Callback
import com.soloist.telegram.keyboard.KeyboardData.Url
import com.soloist.telegram.callback.CallbackData

enum class Keyboard(
	val data: List<KeyboardData>
) {

	INTERRUPT_CONFIRMATION(
		listOf(
			Callback(CallbackData.INTERRUPT_CANCEL),
			Callback(CallbackData.INTERRUPT_CONFIRM)
		)
	),
	DEPRECATE_ALL_TASKS_CONFIRMATION(
		listOf(
			Callback(CallbackData.IDLE_CANCEL),
			Callback(CallbackData.DEPRECATE_ALL_TASKS_CONFIRM)
		)
	),
	DEPRECATE_TASKS_BY_TOPIC_CONFIRMATION(
		listOf(
			Callback(CallbackData.IDLE_CANCEL),
			Callback(CallbackData.DEPRECATE_TASKS_BY_TOPIC_CONFIRM)
		)
	),
	RESET_PLAYER_CONFIRMATION(
		listOf(
			Callback(CallbackData.IDLE_CANCEL),
			Callback(CallbackData.RESET_PLAYER_CONFIRM)
		)
	),
	SEND_NEWSLETTER_CONFIRMATION(
		listOf(
			Callback(CallbackData.IDLE_CANCEL),
			Callback(CallbackData.SEND_NEWSLETTER_CONFIRM)
		)
	),
	MINI_APP_LINK(
		listOf(
			Url(Button.MINI_APP_LINK, "app.telegram.bot.mini-app.url")
		)
	)
}
