package com.sleepkqq.sololeveling.telegram.keyboard

import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode

sealed interface KeyboardAction {

	data class Callback(
		val callbackAction: CallbackAction
	) : KeyboardAction

	data class Url(
		val localizationCode: LocalizationCode,
		val urlProperty: String
	) : KeyboardAction
}
