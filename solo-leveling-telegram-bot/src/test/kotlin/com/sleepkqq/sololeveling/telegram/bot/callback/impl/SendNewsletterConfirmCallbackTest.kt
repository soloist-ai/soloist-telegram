package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.service.broadcast.ScheduledBroadcastService
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.localization.Localized
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter.NewsletterConfirmationState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.message.Message

class SendNewsletterConfirmCallbackTest {

	private lateinit var userSessionService: UserSessionService
	private lateinit var scheduledBroadcastService: ScheduledBroadcastService
	private lateinit var telegramMessageFactory: TelegramMessageFactory
	private lateinit var callback: SendNewsletterConfirmCallback

	@BeforeEach
	fun setUp() {
		userSessionService = mock()
		scheduledBroadcastService = mock()
		telegramMessageFactory = mock()
		callback = SendNewsletterConfirmCallback(
			userSessionService,
			scheduledBroadcastService,
			telegramMessageFactory
		)
	}

	@Test
	fun `action should return SEND_NEWSLETTER_CONFIRM`() {
		// When/Then
		assertThat(callback.action).isEqualTo(CallbackAction.SEND_NEWSLETTER_CONFIRM)
	}

	@Test
	fun `handle should insert newsletter and return edit message`() {
		// Given
		val userId = 123456L
		val messageId = 789

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val state = mock<NewsletterConfirmationState>()
		val botState = mock<BotSessionState>()
		val localized = mock<Localized>()
		val expectedResult = mock<EditMessageText>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(state).thenReturn(botState)
		whenever(botState.onExitLocalized()).thenReturn(localized)
		whenever(telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized
		)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(scheduledBroadcastService).insert(state)
		verify(userSessionService).idleState(userId)
		verify(telegramMessageFactory).editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized
		)
	}

	@Test
	fun `handle should return deleteMessage when state is not NewsletterConfirmationState`() {
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
		whenever(session.state()).thenReturn(mock()) // Wrong state type
		whenever(telegramMessageFactory.deleteMessage(userId, messageId)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(telegramMessageFactory).deleteMessage(userId, messageId)
		verify(scheduledBroadcastService, never()).insert(any())
		verify(userSessionService, never()).idleState(any())
	}

	@Test
	fun `handle should insert newsletter before setting idle state`() {
		// Given
		val userId = 123456L
		val messageId = 789

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val state = mock<NewsletterConfirmationState>()
		val botState = mock<BotSessionState>()
		val localized = mock<Localized>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(state).thenReturn(botState)
		whenever(botState.onExitLocalized()).thenReturn(localized)
		whenever(telegramMessageFactory.editMessageText(any(), any(), any())).thenReturn(mock())

		// When
		callback.handle(callbackQuery, session)

		// Then - verify order of operations
		inOrder(scheduledBroadcastService, userSessionService) {
			verify(scheduledBroadcastService).insert(state)
			verify(userSessionService).idleState(userId)
		}
	}

	@Test
	fun `handle should pass correct state to broadcast service`() {
		// Given
		val userId = 123456L
		val messageId = 789

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val state = mock<NewsletterConfirmationState>()
		val botState = mock<BotSessionState>()
		val localized = mock<Localized>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(state).thenReturn(botState)
		whenever(botState.onExitLocalized()).thenReturn(localized)
		whenever(telegramMessageFactory.editMessageText(any(), any(), any())).thenReturn(mock())

		// When
		callback.handle(callbackQuery, session)

		// Then
		verify(scheduledBroadcastService).insert(state)
	}
}