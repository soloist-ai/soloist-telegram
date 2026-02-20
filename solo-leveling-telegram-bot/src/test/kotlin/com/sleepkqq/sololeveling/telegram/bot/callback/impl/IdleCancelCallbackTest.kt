package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.message.Message

class IdleCancelCallbackTest {

	private lateinit var userSessionService: UserSessionService
	private lateinit var telegramMessageFactory: TelegramMessageFactory
	private lateinit var callback: IdleCancelCallback

	@BeforeEach
	fun setUp() {
		userSessionService = mock()
		telegramMessageFactory = mock()
		callback = IdleCancelCallback(userSessionService, telegramMessageFactory)
	}

	@Test
	fun `action should return IDLE_CANCEL`() {
		// When/Then
		assertThat(callback.action).isEqualTo(CallbackAction.IDLE_CANCEL)
	}

	@Test
	fun `handle should set idle state and return edit message with action canceled`() {
		// Given
		val userId = 123456L
		val messageId = 789

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val expectedResult = mock<EditMessageText>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			code = LocalizationCode.INFO_ACTION_CANCELED
		)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(userSessionService).idleState(userId)
		verify(telegramMessageFactory).editMessageText(
			chatId = userId,
			messageId = messageId,
			code = LocalizationCode.INFO_ACTION_CANCELED
		)
	}

	@Test
	fun `handle should call idleState before editing message`() {
		// Given
		val userId = 123456L
		val messageId = 789

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(telegramMessageFactory.editMessageText(any(), any(), any())).thenReturn(mock())

		// When
		callback.handle(callbackQuery, session)

		// Then - verify order of operations
		inOrder(userSessionService, telegramMessageFactory) {
			verify(userSessionService).idleState(userId)
			verify(telegramMessageFactory).editMessageText(
				chatId = userId,
				messageId = messageId,
				code = LocalizationCode.INFO_ACTION_CANCELED
			)
		}
	}

	@Test
	fun `handle should work with different user IDs`() {
		// Given
		val userId = 999999L
		val messageId = 111

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val expectedResult = mock<EditMessageText>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			code = LocalizationCode.INFO_ACTION_CANCELED
		)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(userSessionService).idleState(userId)
	}
}