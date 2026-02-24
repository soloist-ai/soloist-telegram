package com.soloist.telegram.bot.aop

import com.soloist.config.interceptor.UserContextHolder
import com.soloist.telegram.bot.callback.Callback
import com.soloist.telegram.bot.callback.value
import com.soloist.telegram.bot.command.Command
import com.soloist.telegram.bot.command.value
import com.soloist.telegram.bot.extensions.command
import com.soloist.telegram.bot.service.auth.AuthService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.message.Message

@Aspect
@Order(20)
@Component
class TelegramAccessInterceptor(
	private val authService: AuthService,
	commands: List<Command>,
	callbacks: List<Callback>
) {

	private val log = LoggerFactory.getLogger(javaClass)
	private val commandsMap: Map<String, Command> = commands.associateBy { it.value() }
	private val callbacksMap: Map<String, Callback> = callbacks.associateBy { it.value().data }

	@Before("execution(* com.soloist.telegram.bot.handler.CommandHandler.handle(..))")
	fun checkCommandAccess(joinPoint: JoinPoint) {
		val message = joinPoint.args.firstOrNull() as? Message ?: return
		val command = message.command()?.let { commandsMap[it] } ?: return

		log.info("Command=/{} called by userId={}", command.value(), UserContextHolder.getUserId())

		checkAccess { authService.hasAccess(command) }
	}

	@Before("execution(* com.soloist.telegram.bot.handler.CallbackQueryHandler.handle(..))")
	fun checkCallbackAccess(joinPoint: JoinPoint) {
		val callbackQuery = joinPoint.args.firstOrNull() as? CallbackQuery ?: return
		val callback = callbacksMap[callbackQuery.data] ?: return

		log.info("Callback={} called by userId={}", callbackQuery.data, UserContextHolder.getUserId())

		checkAccess { authService.hasAccess(callback) }
	}

	private fun checkAccess(hasAccess: () -> Boolean) {
		if (!hasAccess()) {
			throw AuthorizationDeniedException("Access denied for userId=${UserContextHolder.getUserId()}")
		}
	}
}
