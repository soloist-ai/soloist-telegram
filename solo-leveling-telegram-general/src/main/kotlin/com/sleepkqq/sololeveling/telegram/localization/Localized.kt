package com.sleepkqq.sololeveling.telegram.localization

import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard

interface Localized {

	val localizationCode: LocalizationCode

	val params: List<Any>
		get() = emptyList()

	val keyboard: Keyboard?
		get() = null
}