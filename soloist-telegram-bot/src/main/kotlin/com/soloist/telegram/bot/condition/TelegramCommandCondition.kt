package com.soloist.telegram.bot.condition

import com.soloist.telegram.bot.annotation.TelegramCommand

class TelegramCommandCondition : TelegramHandlerCondition(
	"app.telegram.bot.commands",
	TelegramCommand::class.java.name
)
