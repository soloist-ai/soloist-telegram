package com.soloist.telegram.keyboard

import com.soloist.telegram.callback.CallbackData

sealed interface KeyboardData {

	val button: Button

	data class Callback(val callbackData: CallbackData) : KeyboardData {
		override val button: Button get() = callbackData.button
	}

	data class Url(override val button: Button, val urlProperty: String) : KeyboardData
}
