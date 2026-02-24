package com.soloist.telegram.bot.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import com.soloist.telegram.bot.config.properties.TelegramBotProperties

@Component
class TelegramWebhookSecurityFilter(
	private val telegramBotProperties: TelegramBotProperties
) : OncePerRequestFilter() {

	private val log = LoggerFactory.getLogger(javaClass)

	private companion object {
		const val TELEGRAM_SECRET_TOKEN_HEADER = "X-Telegram-Bot-Api-Secret-Token"
	}

	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		if (!request.requestURI.contains("/${telegramBotProperties.webhook.path}")) {
			filterChain.doFilter(request, response)
			return
		}

		val receivedToken = request.getHeader(TELEGRAM_SECRET_TOKEN_HEADER)
		val expectedToken = telegramBotProperties.webhook.secretToken

		if (receivedToken.isNullOrBlank() || receivedToken != expectedToken) {
			log.warn("Invalid or missing secret token from IP: ${request.remoteAddr}")
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid secret token")
			return
		}

		log.debug("Valid secret token received, processing webhook")
		filterChain.doFilter(request, response)
	}
}
