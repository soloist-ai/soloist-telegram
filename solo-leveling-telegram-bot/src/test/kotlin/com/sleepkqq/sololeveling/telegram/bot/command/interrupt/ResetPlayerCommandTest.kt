package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.grpc.client.UserApi
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.IdleState
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.player.ResetPlayerConfirmationState
import io.grpc.StatusRuntimeException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.objects.message.Message

class ResetPlayerCommandTest {

	private lateinit var userSessionService: UserSessionService
	private lateinit var userApi: UserApi
	private lateinit var command: ResetPlayerCommand

	@BeforeEach
	fun setUp() {
		userSessionService = mock()
		userApi = mock()
		command = ResetPlayerCommand(userSessionService, userApi)
	}

	@Test
	fun `command should return reset_player command string`() {
		// When/Then
		assertThat(command.command).isEqualTo("/reset_player")
	}

	@Test
	fun `visible should return false`() {
		// When/Then
		assertThat(command.visible).isFalse()
	}

	@Test
	fun `createState should return ResetPlayerConfirmationState with valid userId`() {
		// Given
		val userId = 123456L
		val message = mock<Message>()
		val session = mock<UserSession>()
		val userResponse = mock<com.sleepkqq.sololeveling.grpc.UserResponse>()
		val user = mock<com.sleepkqq.sololeveling.grpc.User>()

		whenever(message.text).thenReturn("/reset_player $userId")
		whenever(userApi.getUser(userId)).thenReturn(userResponse)
		whenever(userResponse.user).thenReturn(user)
		whenever(user.username).thenReturn("testuser")
		whenever(user.firstName).thenReturn("Test")
		whenever(user.lastName).thenReturn("User")

		// When
		val result = command.createState(message, session)

		// Then
		assertThat(result).isNotNull
		assertThat(result).isInstanceOf(ResetPlayerConfirmationState::class.java)
		assertThat(result?.id).isEqualTo(userId)
		verify(userApi).getUser(userId)
	}

	@Test
	fun `createState should return null when text is null`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()

		whenever(message.text).thenReturn(null)

		// When
		val result = command.createState(message, session)

		// Then
		assertThat(result).isNull()
		verify(userApi, never()).getUser(any())
	}

	@Test
	fun `createState should return null when command has no arguments`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()

		whenever(message.text).thenReturn("/reset_player")

		// When
		val result = command.createState(message, session)

		// Then
		assertThat(result).isNull()
		verify(userApi, never()).getUser(any())
	}

	@Test
	fun `createState should return null when userId is invalid`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()

		whenever(message.text).thenReturn("/reset_player invalid")

		// When
		val result = command.createState(message, session)

		// Then
		assertThat(result).isNull()
		verify(userApi, never()).getUser(any())
	}

	@Test
	fun `createState should return null when userId is negative`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()

		whenever(message.text).thenReturn("/reset_player -123")

		// When
		val result = command.createState(message, session)

		// Then
		assertThat(result).isNull()
		verify(userApi, never()).getUser(any())
	}

	@Test
	fun `createState should return null when userId is zero`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()

		whenever(message.text).thenReturn("/reset_player 0")

		// When
		val result = command.createState(message, session)

		// Then
		assertThat(result).isNull()
		verify(userApi, never()).getUser(any())
	}

	@Test
	fun `createState should return null when too many arguments provided`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()

		whenever(message.text).thenReturn("/reset_player 123 456")

		// When
		val result = command.createState(message, session)

		// Then
		assertThat(result).isNull()
		verify(userApi, never()).getUser(any())
	}

	@Test
	fun `createState should return null when userApi throws exception`() {
		// Given
		val userId = 123456L
		val message = mock<Message>()
		val session = mock<UserSession>()

		whenever(message.text).thenReturn("/reset_player $userId")
		whenever(userApi.getUser(userId)).thenThrow(StatusRuntimeException(io.grpc.Status.NOT_FOUND))

		// When
		val result = command.createState(message, session)

		// Then
		assertThat(result).isNull()
		verify(userApi).getUser(userId)
	}

	@Test
	fun `createState should handle whitespace correctly`() {
		// Given
		val userId = 123456L
		val message = mock<Message>()
		val session = mock<UserSession>()
		val userResponse = mock<com.sleepkqq.sololeveling.grpc.UserResponse>()
		val user = mock<com.sleepkqq.sololeveling.grpc.User>()

		whenever(message.text).thenReturn("  /reset_player   $userId  ")
		whenever(userApi.getUser(userId)).thenReturn(userResponse)
		whenever(userResponse.user).thenReturn(user)
		whenever(user.username).thenReturn("testuser")
		whenever(user.firstName).thenReturn("Test")
		whenever(user.lastName).thenReturn("User")

		// When
		val result = command.createState(message, session)

		// Then
		assertThat(result).isNotNull
		assertThat(result?.id).isEqualTo(userId)
	}

	@Test
	fun `handle should return StateChanged when session is in IdleState and valid state`() {
		// Given
		val userId = 123456L
		val message = mock<Message>()
		val session = mock<UserSession>()
		val idleState = mock<IdleState>()
		val userResponse = mock<com.sleepkqq.sololeveling.grpc.UserResponse>()
		val user = mock<com.sleepkqq.sololeveling.grpc.User>()

		whenever(message.text).thenReturn("/reset_player $userId")
		whenever(session.state()).thenReturn(idleState)
		whenever(userApi.getUser(userId)).thenReturn(userResponse)
		whenever(userResponse.user).thenReturn(user)
		whenever(user.username).thenReturn("testuser")
		whenever(user.firstName).thenReturn("Test")
		whenever(user.lastName).thenReturn("User")

		// When
		val result = command.handle(message, session)

		// Then
		assertThat(result).isNotNull
		assertThat(result).isInstanceOf(InterruptCommand.InterruptCommandResult.StateChanged::class.java)
	}

	@Test
	fun `handle should return null when createState returns null`() {
		// Given
		val message = mock<Message>()
		val session = mock<UserSession>()
		val idleState = mock<IdleState>()

		whenever(message.text).thenReturn("/reset_player invalid")
		whenever(session.state()).thenReturn(idleState)

		// When
		val result = command.handle(message, session)

		// Then
		assertThat(result).isNull()
	}
}