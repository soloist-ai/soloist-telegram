package com.soloist.telegram.bot.aop

import com.soloist.config.interceptor.UserContextHolder
import com.soloist.telegram.bot.service.auth.AuthService
import com.soloist.telegram.bot.service.message.TelegramMessageFactory
import com.soloist.telegram.localization.ErrorCode
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.annotation.Order
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

@Aspect
@Order(10)
@Component
class TelegramUpdateInterceptor(
	private val authService: AuthService,
	private val telegramMessageFactory: TelegramMessageFactory,
) {

	private val log = LoggerFactory.getLogger(javaClass)

	@Around("execution(* com.soloist.telegram.bot.dispatcher.UpdateDispatcher.dispatch(..)) && args(update)")
	fun interceptUpdateDispatch(pjp: ProceedingJoinPoint, update: Update): BotApiMethod<*>? {
		val start = System.currentTimeMillis()
		val userId = try {
			authService.login(update)
			UserContextHolder.getUserId()!!

		} catch (e: Exception) {
			log.error("Error during login", e)
			return null
		}

		try {
			return pjp.proceed() as? BotApiMethod<*>

		} catch (e: AuthorizationDeniedException) {
			log.warn("Authorization denied userId={}: {}", userId, e.message)
			return telegramMessageFactory.sendMessage(userId, ErrorCode.ACCESS_DENIED)

		} catch (ex: Exception) {
			log.error("Error processing telegram updateId={} by userId={}", update.updateId, userId, ex)
			return null

		} finally {
			val elapsed = System.currentTimeMillis() - start
			log.info("Processed updateId={} by userId={} in {}ms", update.updateId, userId, elapsed)
			clearContexts()
		}
	}

	private fun clearContexts() {
		try {
			SecurityContextHolder.clearContext()
			LocaleContextHolder.resetLocaleContext()
			UserContextHolder.clear()

		} catch (ex: Exception) {
			log.warn("Error clearing thread-local contexts", ex)
		}
	}
}
