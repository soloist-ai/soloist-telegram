package com.soloist.telegram.localization

enum class ButtonCode(override val path: String) : LocalizationCode {
	CONFIRM("button.confirm"),
	CANCEL("button.cancel"),
	MINI_APP_LINK("button.mini-app.link")
}
