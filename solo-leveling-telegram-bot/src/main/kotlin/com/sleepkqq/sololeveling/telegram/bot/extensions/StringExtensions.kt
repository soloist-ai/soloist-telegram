package com.sleepkqq.sololeveling.telegram.bot.extensions

fun String.toKebabCase(): String = this.replace("_", "-").lowercase()
