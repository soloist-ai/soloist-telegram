package com.soloist.telegram.localization

data class Suggestions<T>(
	val items: List<T>,
	val label: (T) -> String,
	val columns: Int = 1
) {
	companion object {
		fun <T> of(items: List<T>, columns: Int = 1) =
			Suggestions(items, { it.toString() }, columns)
	}
}