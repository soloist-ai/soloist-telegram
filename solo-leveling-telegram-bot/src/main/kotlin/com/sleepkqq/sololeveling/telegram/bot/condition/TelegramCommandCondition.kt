package com.sleepkqq.sololeveling.telegram.bot.condition

import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCommand

class TelegramCommandCondition : TelegramHandlerCondition(
	"app.telegram.bot.commands",
	TelegramCommand::class.java.name
)
