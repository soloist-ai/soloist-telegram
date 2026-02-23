package com.sleepkqq.sololeveling.telegram.bot.annotation

import com.sleepkqq.sololeveling.telegram.bot.condition.TelegramCallbackCondition
import com.sleepkqq.sololeveling.telegram.callback.CallbackData
import org.springframework.context.annotation.Conditional
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
@Conditional(TelegramCallbackCondition::class)
annotation class TelegramCallback(val value: CallbackData)
