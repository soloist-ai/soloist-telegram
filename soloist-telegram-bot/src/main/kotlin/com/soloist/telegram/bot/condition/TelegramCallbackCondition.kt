package com.soloist.telegram.bot.condition

import com.soloist.telegram.bot.annotation.TelegramCallback

class TelegramCallbackCondition : TelegramHandlerCondition(
	"app.telegram.bot.callbacks",
	TelegramCallback::class.java.name
)
