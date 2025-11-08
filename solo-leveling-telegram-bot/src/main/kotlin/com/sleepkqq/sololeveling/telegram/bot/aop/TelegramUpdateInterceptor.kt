package com.sleepkqq.sololeveling.telegram.bot.aop

import com.sleepkqq.sololeveling.telegram.bot.service.auth.AuthService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

@Aspect
@Component
class TelegramUpdateInterceptor(
	private val authService: AuthService
) {

	@Around("execution(* com.sleepkqq.sololeveling.telegram.bot.dispatcher.UpdateDispatcher.dispatch(..)) && args(update)")
	fun interceptUpdateDispatch(pjp: ProceedingJoinPoint, update: Update): Any? {
		authService.login(update)

		return pjp.proceed()
	}
}
