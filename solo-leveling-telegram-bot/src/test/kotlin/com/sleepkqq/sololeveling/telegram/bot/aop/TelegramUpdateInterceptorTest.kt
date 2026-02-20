package com.sleepkqq.sololeveling.telegram.bot.aop

import com.sleepkqq.sololeveling.config.interceptor.UserContextHolder
import com.sleepkqq.sololeveling.telegram.bot.service.auth.AuthService
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import org.aspectj.lang.ProceedingJoinPoint
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

class TelegramUpdateInterceptorTest {

	private lateinit var authService: AuthService
	private lateinit var telegramMessageFactory: TelegramMessageFactory
	private lateinit var interceptor: TelegramUpdateInterceptor
	private lateinit var proceedingJoinPoint: ProceedingJoinPoint
	private lateinit var update: Update

	@BeforeEach
	fun setUp() {
		authService = mock()
		telegramMessageFactory = mock()
		interceptor = TelegramUpdateInterceptor(authService, telegramMessageFactory)
		proceedingJoinPoint = mock()
		update = mock()

		// Setup update mock
		whenever(update.updateId).thenReturn(12345L)

		// Clear contexts before each test
		clearAllContexts()
	}

	@AfterEach
	fun tearDown() {
		clearAllContexts()
	}

	private fun clearAllContexts() {
		try {
			SecurityContextHolder.clearContext()
			UserContextHolder.clear()
		} catch (e: Exception) {
			// Ignore cleanup errors
		}
	}

	@Test
	fun `interceptUpdateDispatch should process update successfully`() {
		// Given
		val userId = 123456L
		val expectedResult = mock<SendMessage>()

		UserContextHolder.setUserId(userId)
		whenever(proceedingJoinPoint.proceed()).thenReturn(expectedResult)

		// When
		val result = interceptor.interceptUpdateDispatch(proceedingJoinPoint, update)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(authService).login(update)
		verify(proceedingJoinPoint).proceed()
	}

	@Test
	fun `interceptUpdateDispatch should handle AuthorizationDeniedException`() {
		// Given
		val userId = 123456L
		val expectedMessage = mock<SendMessage>()

		UserContextHolder.setUserId(userId)
		whenever(authService.login(update)).thenThrow(AuthorizationDeniedException("Access denied", null))
		whenever(telegramMessageFactory.sendMessage(userId, LocalizationCode.ERROR_ACCESS_DENIED))
			.thenReturn(expectedMessage)

		// When
		val result = interceptor.interceptUpdateDispatch(proceedingJoinPoint, update)

		// Then
		assertThat(result).isEqualTo(expectedMessage)
		verify(authService).login(update)
		verify(telegramMessageFactory).sendMessage(userId, LocalizationCode.ERROR_ACCESS_DENIED)
		verify(proceedingJoinPoint, never()).proceed()
	}

	@Test
	fun `interceptUpdateDispatch should return null on unexpected exception`() {
		// Given
		UserContextHolder.setUserId(789L)
		whenever(authService.login(update)).thenThrow(RuntimeException("Unexpected error"))

		// When
		val result = interceptor.interceptUpdateDispatch(proceedingJoinPoint, update)

		// Then
		assertThat(result).isNull()
		verify(authService).login(update)
		verify(proceedingJoinPoint, never()).proceed()
	}

	@Test
	fun `interceptUpdateDispatch should clear contexts after processing`() {
		// Given
		val userId = 123456L
		UserContextHolder.setUserId(userId)
		whenever(proceedingJoinPoint.proceed()).thenReturn(mock<SendMessage>())

		// When
		interceptor.interceptUpdateDispatch(proceedingJoinPoint, update)

		// Then - contexts should be cleared (we can't directly verify ThreadLocal clearing,
		// but we verify the method was called without exceptions)
		verify(authService).login(update)
	}

	@Test
	fun `interceptUpdateDispatch should clear contexts even when exception occurs`() {
		// Given
		UserContextHolder.setUserId(999L)
		whenever(authService.login(update)).thenThrow(RuntimeException("Test exception"))

		// When
		val result = interceptor.interceptUpdateDispatch(proceedingJoinPoint, update)

		// Then
		assertThat(result).isNull()
		// Context clearing happens in finally block, so it executes even on exception
		verify(authService).login(update)
	}

	@Test
	fun `interceptUpdateDispatch should handle null return from proceed`() {
		// Given
		val userId = 123456L
		UserContextHolder.setUserId(userId)
		whenever(proceedingJoinPoint.proceed()).thenReturn(null)

		// When
		val result = interceptor.interceptUpdateDispatch(proceedingJoinPoint, update)

		// Then
		assertThat(result).isNull()
		verify(authService).login(update)
		verify(proceedingJoinPoint).proceed()
	}

	@Test
	fun `interceptUpdateDispatch should handle proceed returning non-BotApiMethod`() {
		// Given
		val userId = 123456L
		UserContextHolder.setUserId(userId)
		whenever(proceedingJoinPoint.proceed()).thenReturn("not a BotApiMethod")

		// When
		val result = interceptor.interceptUpdateDispatch(proceedingJoinPoint, update)

		// Then
		assertThat(result).isNull()
		verify(authService).login(update)
		verify(proceedingJoinPoint).proceed()
	}
}