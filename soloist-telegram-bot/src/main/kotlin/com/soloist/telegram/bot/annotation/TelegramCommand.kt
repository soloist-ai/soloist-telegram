package com.soloist.telegram.bot.annotation

import com.soloist.telegram.bot.condition.TelegramCommandCondition
import com.soloist.telegram.localization.CommandDescriptionCode
import org.springframework.context.annotation.Conditional
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
@Conditional(TelegramCommandCondition::class)
annotation class TelegramCommand(val value: String, val description: CommandDescriptionCode)
