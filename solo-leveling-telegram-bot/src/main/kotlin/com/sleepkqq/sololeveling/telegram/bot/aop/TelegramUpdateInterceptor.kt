package com.sleepkqq.sololeveling.telegram.bot.aop

import com.sleepkqq.sololeveling.config.interceptor.UserContextHolder
import com.sleepkqq.sololeveling.telegram.bot.service.auth.AuthService
import com.sleepkqq.sololeveling.telegram.bot.service.localization.I18nService
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

@Aspect
@Component
class TelegramUpdateInterceptor(
	private val authService: AuthService,
	private val i18nService: I18nService
) {

	private val log = LoggerFactory.getLogger(javaClass)

	@Around("execution(* com.sleepkqq.sololeveling.telegram.bot.dispatcher.UpdateDispatcher.dispatch(..)) && args(update)")
	fun interceptUpdateDispatch(pjp: ProceedingJoinPoint, update: Update): BotApiMethod<*>? {
		try {
			authService.login(update)
			val userId = UserContextHolder.getUserId()

			log.info("Processing telegram updateId={}, userId={}", update.updateId, userId)

			return pjp.proceed() as BotApiMethod<*>?

		} catch (e: AuthorizationDeniedException) {
			val userId = UserContextHolder.getUserId()!!

			log.warn("Authorization denied userId={}: {}", userId, e.message)
			return i18nService.sendMessage(userId, LocalizationCode.ERROR_ACCESS_DENIED)

		} catch (ex: Exception) {
			log.error(
				"Error processing telegram updateId={}, userId={}",
				update.updateId,
				UserContextHolder.getUserId(),
				ex
			)
			return null

		} finally {
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
