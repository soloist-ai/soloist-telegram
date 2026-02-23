package com.sleepkqq.sololeveling.telegram.bot.condition

import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCallback

class TelegramCallbackCondition : TelegramHandlerCondition(
	"app.telegram.bot.callbacks",
	TelegramCallback::class.java.name
)
