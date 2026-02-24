package com.soloist.telegram.bot.extensions

fun String.toKebabCase(): String = this.replace("_", "-").lowercase()
