package com.sleepkqq.sololeveling.telegram.bot.extensions

import org.telegram.telegrambots.meta.api.objects.message.Message

fun Message.command(): String = this.text.split(" ").first().removePrefix("/")
