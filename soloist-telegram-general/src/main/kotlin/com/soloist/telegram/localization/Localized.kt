package com.soloist.telegram.localization

import com.soloist.telegram.keyboard.Keyboard

interface Localized {

	val localizationCode: LocalizationCode

	val params: List<Any>
		get() = emptyList()

	val keyboard: Keyboard?
		get() = null

	val suggestions: Suggestions<*>?
		get() = null
}