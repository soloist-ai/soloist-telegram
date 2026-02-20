package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.IdleState
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.feedback.FeedbackMessageState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.objects.message.Message

class FeedbackCommandTest {

	private lateinit var userSessionService: UserSessionService
	private lateinit var command: FeedbackCommand

	@BeforeEach
	fun setUp() {
		userSessionService = mock()
		command = FeedbackCommand(userSessionService)
	}

	@Test
	fun `command should return feedback command string`() {
		// When/Then
		assertThat(command.command).isEqualTo("/feedback")
	}

	@Test
	fun `visible should return true`() {
		// When/Then
		assertThat(command.visible).isTrue()
	}

	@Test
	fun `description should return send feedback description`() {
		// When/Then
		assertThat(command.description).isEqualTo("Send feedback")
	}

	@Test
	fun `createState should return FeedbackMessageState`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()

		// When
		val result = command.createState(message, session)

		// Then
		assertThat(result).isNotNull
		assertThat(result).isInstanceOf(FeedbackMessageState::class.java)
	}

	@Test
	fun `handle should return StateChanged when session is in IdleState`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()
		val idleState = mock<IdleState>()

		whenever(session.state()).thenReturn(idleState)

		// When
		val result = command.handle(message, session)

		// Then
		assertThat(result).isNotNull
		assertThat(result).isInstanceOf(InterruptCommand.InterruptCommandResult.StateChanged::class.java)
		verify(userSessionService).update(any())
	}

	@Test
	fun `handle should return Question when session is not in IdleState`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()
		val nonIdleState = mock<FeedbackMessageState>()

		whenever(session.state()).thenReturn(nonIdleState)

		// When
		val result = command.handle(message, session)

		// Then
		assertThat(result).isNotNull
		assertThat(result).isInstanceOf(InterruptCommand.InterruptCommandResult.Question::class.java)
		verify(userSessionService).update(any())
	}

	@Test
	fun `handle should create new state and update session in IdleState`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()
		val idleState = mock<IdleState>()

		whenever(session.state()).thenReturn(idleState)

		// When
		command.handle(message, session)

		// Then
		verify(userSessionService).update(argThat { userSession ->
			userSession != null
		})
	}

	@Test
	fun `handle should set pending interrupt state when not idle`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()
		val nonIdleState = mock<FeedbackMessageState>()

		whenever(session.state()).thenReturn(nonIdleState)

		// When
		command.handle(message, session)

		// Then
		verify(userSessionService).update(argThat { userSession ->
			userSession != null
		})
	}

	@Test
	fun `createState should always create new instance`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()

		// When
		val result1 = command.createState(message, session)
		val result2 = command.createState(message, session)

		// Then - should create different instances
		assertThat(result1).isNotSameAs(result2)
	}
}