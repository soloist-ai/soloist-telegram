package com.sleepkqq.sololeveling.telegram.bot.callback

import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCallback
import com.sleepkqq.sololeveling.telegram.callback.CallbackData
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import org.springframework.aop.support.AopUtils
import org.springframework.core.annotation.AnnotationUtils
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import java.util.concurrent.ConcurrentHashMap

interface Callback {

	fun handle(callbackQuery: CallbackQuery, session: UserSession): BotApiMethod<*>?
}

private val callbackValueCache = ConcurrentHashMap<Class<*>, CallbackData>()

fun Callback.value(): CallbackData = AopUtils.getTargetClass(this).let {
	callbackValueCache.getOrPut(it) {
		AnnotationUtils.findAnnotation(it, TelegramCallback::class.java)
			?.value
			?: error("${javaClass.simpleName} missing @TelegramCallback")
	}
}
