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

class InterruptCancelCallbackTest {

	private lateinit var userSessionService: UserSessionService
	private lateinit var telegramMessageFactory: TelegramMessageFactory
	private lateinit var callback: InterruptCancelCallback

	@BeforeEach
	fun setUp() {
		userSessionService = mock()
		telegramMessageFactory = mock()
		callback = InterruptCancelCallback(userSessionService, telegramMessageFactory)
	}

	@Test
	fun `action should return INTERRUPT_CANCEL`() {
		// When/Then
		assertThat(callback.action).isEqualTo(CallbackAction.INTERRUPT_CANCEL)
	}

	@Test
	fun `handle should cancel interrupt state and return edit message`() {
		// Given
		val userId = 123456L
		val messageId = 789

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val state = mock<BotSessionState>()
		val localized = mock<Localized>()
		val expectedResult = mock<EditMessageText>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(state)
		whenever(state.onEnterLocalized()).thenReturn(localized)
		whenever(telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized
		)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(userSessionService).cancelInterruptState(userId)
		verify(telegramMessageFactory).editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized
		)
	}

	@Test
	fun `handle should cancel interrupt before editing message`() {
		// Given
		val userId = 123456L
		val messageId = 789

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val state = mock<BotSessionState>()
		val localized = mock<Localized>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(state)
		whenever(state.onEnterLocalized()).thenReturn(localized)
		whenever(telegramMessageFactory.editMessageText(any(), any(), any())).thenReturn(mock())

		// When
		callback.handle(callbackQuery, session)

		// Then - verify order of operations
		inOrder(userSessionService, telegramMessageFactory) {
			verify(userSessionService).cancelInterruptState(userId)
			verify(telegramMessageFactory).editMessageText(
				chatId = userId,
				messageId = messageId,
				localized = localized
			)
		}
	}

	@Test
	fun `handle should use onEnterLocalized from current state`() {
		// Given
		val userId = 123456L
		val messageId = 789

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val state = mock<BotSessionState>()
		val localized = mock<Localized>()
		val expectedResult = mock<EditMessageText>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(state)
		whenever(state.onEnterLocalized()).thenReturn(localized)
		whenever(telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized
		)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(state).onEnterLocalized()
	}
}