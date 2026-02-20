package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.localization.Localized
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.message.Message

class InterruptConfirmCallbackTest {

	private lateinit var userSessionService: UserSessionService
	private lateinit var telegramMessageFactory: TelegramMessageFactory
	private lateinit var callback: InterruptConfirmCallback

	@BeforeEach
	fun setUp() {
		userSessionService = mock()
		telegramMessageFactory = mock()
		callback = InterruptConfirmCallback(userSessionService, telegramMessageFactory)
	}

	@Test
	fun `action should return INTERRUPT_CONFIRM`() {
		// When/Then
		assertThat(callback.action).isEqualTo(CallbackAction.INTERRUPT_CONFIRM)
	}

	@Test
	fun `handle should confirm interrupt state and return edit message`() {
		// Given
		val userId = 123456L
		val messageId = 789

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val pendingInterruptState = mock<BotSessionState>()
		val localized = mock<Localized>()
		val expectedResult = mock<EditMessageText>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.pendingInterruptState()).thenReturn(pendingInterruptState)
		whenever(pendingInterruptState.onEnterLocalized()).thenReturn(localized)
		whenever(telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized
		)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(userSessionService).confirmInterruptState(userId)
		verify(telegramMessageFactory).editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized
		)
	}

	@Test
	fun `handle should return deleteMessage when pendingInterruptState is null`() {
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
		whenever(session.pendingInterruptState()).thenReturn(null)
		whenever(telegramMessageFactory.deleteMessage(userId, messageId)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(telegramMessageFactory).deleteMessage(userId, messageId)
		verify(userSessionService, never()).confirmInterruptState(any())
	}

	@Test
	fun `handle should confirm interrupt before editing message`() {
		// Given
		val userId = 123456L
		val messageId = 789

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val pendingInterruptState = mock<BotSessionState>()
		val localized = mock<Localized>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.pendingInterruptState()).thenReturn(pendingInterruptState)
		whenever(pendingInterruptState.onEnterLocalized()).thenReturn(localized)
		whenever(telegramMessageFactory.editMessageText(any(), any(), any())).thenReturn(mock())

		// When
		callback.handle(callbackQuery, session)

		// Then - verify order of operations
		inOrder(userSessionService, telegramMessageFactory) {
			verify(userSessionService).confirmInterruptState(userId)
			verify(telegramMessageFactory).editMessageText(
				chatId = userId,
				messageId = messageId,
				localized = localized
			)
		}
	}

	@Test
	fun `handle should use onEnterLocalized from pending interrupt state`() {
		// Given
		val userId = 123456L
		val messageId = 789

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val pendingInterruptState = mock<BotSessionState>()
		val localized = mock<Localized>()
		val expectedResult = mock<EditMessageText>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.pendingInterruptState()).thenReturn(pendingInterruptState)
		whenever(pendingInterruptState.onEnterLocalized()).thenReturn(localized)
		whenever(telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized
		)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(pendingInterruptState).onEnterLocalized()
	}
}