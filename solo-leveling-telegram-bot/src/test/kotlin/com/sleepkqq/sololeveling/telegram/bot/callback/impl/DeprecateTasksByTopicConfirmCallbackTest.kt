package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.grpc.client.PlayerApi
import com.sleepkqq.sololeveling.telegram.bot.mapper.ProtoMapper
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.localization.Localized
import com.sleepkqq.sololeveling.telegram.model.entity.player.TaskTopic
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.task.DeprecateTasksByTopicConfirmationState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.message.Message

class DeprecateTasksByTopicConfirmCallbackTest {

	private lateinit var userSessionService: UserSessionService
	private lateinit var playerApi: PlayerApi
	private lateinit var telegramMessageFactory: TelegramMessageFactory
	private lateinit var protoMapper: ProtoMapper
	private lateinit var callback: DeprecateTasksByTopicConfirmCallback

	@BeforeEach
	fun setUp() {
		userSessionService = mock()
		playerApi = mock()
		telegramMessageFactory = mock()
		protoMapper = mock()
		callback = DeprecateTasksByTopicConfirmCallback(
			userSessionService,
			playerApi,
			telegramMessageFactory,
			protoMapper
		)
	}

	@Test
	fun `action should return DEPRECATE_TASKS_BY_TOPIC_CONFIRM`() {
		// When/Then
		assertThat(callback.action).isEqualTo(CallbackAction.DEPRECATE_TASKS_BY_TOPIC_CONFIRM)
	}

	@Test
	fun `handle should deprecate tasks by topic and return edit message`() {
		// Given
		val userId = 123456L
		val messageId = 789
		val effectedRows = 15L
		val taskTopic = TaskTopic.DAILY_QUEST

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val state = mock<DeprecateTasksByTopicConfirmationState>()
		val localized = mock<Localized>()
		val protoTaskTopic = mock<com.sleepkqq.sololeveling.grpc.TaskTopic>()
		val expectedResult = mock<EditMessageText>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(state)
		whenever(state.taskTopic()).thenReturn(taskTopic)
		whenever(state.onExitLocalized()).thenReturn(localized)
		whenever(protoMapper.map(taskTopic)).thenReturn(protoTaskTopic)
		whenever(playerApi.deprecateTasksByTopic(protoTaskTopic)).thenReturn(effectedRows)
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
		verify(protoMapper).map(taskTopic)
		verify(playerApi).deprecateTasksByTopic(protoTaskTopic)
		verify(telegramMessageFactory).editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = localized,
			params = listOf(effectedRows)
		)
	}

	@Test
	fun `handle should return deleteMessage when state is not DeprecateTasksByTopicConfirmationState`() {
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
		verify(playerApi, never()).deprecateTasksByTopic(any())
		verify(userSessionService, never()).idleState(any())
	}

	@Test
	fun `handle should set idle state before deprecating tasks`() {
		// Given
		val userId = 123456L
		val messageId = 789
		val taskTopic = TaskTopic.DAILY_QUEST

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val state = mock<DeprecateTasksByTopicConfirmationState>()
		val localized = mock<Localized>()
		val protoTaskTopic = mock<com.sleepkqq.sololeveling.grpc.TaskTopic>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(userId)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(state)
		whenever(state.taskTopic()).thenReturn(taskTopic)
		whenever(state.onExitLocalized()).thenReturn(localized)
		whenever(protoMapper.map(taskTopic)).thenReturn(protoTaskTopic)
		whenever(playerApi.deprecateTasksByTopic(protoTaskTopic)).thenReturn(5L)
		whenever(telegramMessageFactory.editMessageText(any(), any(), any(), any())).thenReturn(mock())

		// When
		callback.handle(callbackQuery, session)

		// Then - verify order of operations
		inOrder(userSessionService, playerApi) {
			verify(userSessionService).idleState(userId)
			verify(playerApi).deprecateTasksByTopic(protoTaskTopic)
		}
	}
}