package com.sleepkqq.sololeveling.telegram.bot.callback

import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCallback
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import org.springframework.aop.support.AopUtils
import org.springframework.core.annotation.AnnotationUtils
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import java.util.concurrent.ConcurrentHashMap

interface Callback {

	fun handle(callbackQuery: CallbackQuery, session: UserSession): BotApiMethod<*>?
}

private val callbackValueCache = ConcurrentHashMap<Class<*>, CallbackAction>()

fun Callback.value(): CallbackAction =
	callbackValueCache.getOrPut(AopUtils.getTargetClass(this)) {
		AnnotationUtils.findAnnotation(AopUtils.getTargetClass(this), TelegramCallback::class.java)
			?.value
			?: error("${javaClass.simpleName} missing @TelegramCallback")
	}
