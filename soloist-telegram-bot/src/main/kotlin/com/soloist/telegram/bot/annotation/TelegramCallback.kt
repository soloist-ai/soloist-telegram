package com.soloist.telegram.bot.annotation

import com.soloist.telegram.bot.condition.TelegramCallbackCondition
import com.soloist.telegram.callback.CallbackData
import org.springframework.context.annotation.Conditional
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
@Conditional(TelegramCallbackCondition::class)
annotation class TelegramCallback(val value: CallbackData)
