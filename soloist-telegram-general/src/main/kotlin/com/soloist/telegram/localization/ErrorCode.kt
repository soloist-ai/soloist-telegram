package com.soloist.telegram.localization

enum class ErrorCode(override val path: String) : LocalizationCode {
	ACCESS_DENIED("error.access-denied"),
	USER_NOT_FOUND("error.user.not-found")
}
