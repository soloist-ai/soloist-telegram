package com.sleepkqq.sololeveling.telegram.keyboard

import com.sleepkqq.sololeveling.telegram.callback.CallbackData

sealed interface KeyboardData {

	val button: Button

	data class Callback(val callbackData: CallbackData) : KeyboardData {
		override val button: Button get() = callbackData.button
	}

	data class Url(override val button: Button, val urlProperty: String) : KeyboardData
}
