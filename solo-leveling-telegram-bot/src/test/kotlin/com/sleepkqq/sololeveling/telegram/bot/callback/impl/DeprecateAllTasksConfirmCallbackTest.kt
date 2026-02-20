package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.grpc.client.PlayerApi
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

class DeprecateAllTasksConfirmCallbackTest {

	private lateinit var userSessionService: UserSessionService
	private lateinit var playerApi: PlayerApi
	private lateinit var telegramMessageFactory: TelegramMessageFactory
	private lateinit var callback: DeprecateAllTasksConfirmCallback

	@BeforeEach
	fun setUp() {
		userSessionService = mock()
		playerApi = mock()
		telegramMessageFactory = mock()
		callback = DeprecateAllTasksConfirmCallback(userSessionService, playerApi, telegramMessageFactory)
	}

	@Test
	fun `action should return DEPRECATE_ALL_TASKS_CONFIRM`() {
		// When/Then
		assertThat(callback.action).isEqualTo(CallbackAction.DEPRECATE_ALL_TASKS_CONFIRM)
	}

	@Test
	fun `handle should deprecate all tasks and return edit message`() {
		// Given
		val userId = 123456L
		val messageId = 789
		val effectedRows = 42L

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
		whenever(state.onExitLocalized()).thenReturn(localized)
		whenever(playerApi.deprecateAllTasks()).thenReturn(effectedRows)
		whenever(telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized,
			params = listOf(effectedRows)
		)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(userSessionService).idleState(userId)
		verify(playerApi).deprecateAllTasks()
		verify(telegramMessageFactory).editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized,
			params = listOf(effectedRows)
		)
	}

	@Test
	fun `handle should set idle state before deprecating tasks`() {
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
		whenever(state.onExitLocalized()).thenReturn(localized)
		whenever(playerApi.deprecateAllTasks()).thenReturn(10L)
		whenever(telegramMessageFactory.editMessageText(any(), any(), any(), any())).thenReturn(mock())

		// When
		callback.handle(callbackQuery, session)

		// Then - verify order of operations
		inOrder(userSessionService, playerApi) {
			verify(userSessionService).idleState(userId)
			verify(playerApi).deprecateAllTasks()
		}
	}

	@Test
	fun `handle should work with zero effected rows`() {
		// Given
		val userId = 123456L
		val messageId = 789
		val effectedRows = 0L

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
		whenever(state.onExitLocalized()).thenReturn(localized)
		whenever(playerApi.deprecateAllTasks()).thenReturn(effectedRows)
		whenever(telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized,
			params = listOf(effectedRows)
		)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(playerApi).deprecateAllTasks()
	}
}