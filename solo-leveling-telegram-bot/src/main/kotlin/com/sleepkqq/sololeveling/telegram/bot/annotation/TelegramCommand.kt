package com.sleepkqq.sololeveling.telegram.bot.annotation

import com.sleepkqq.sololeveling.telegram.bot.condition.TelegramCommandCondition
import com.sleepkqq.sololeveling.telegram.localization.CommandDescriptionCode
import org.springframework.context.annotation.Conditional
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
@Conditional(TelegramCommandCondition::class)
annotation class TelegramCommand(val value: String, val description: CommandDescriptionCode)
