package com.sleepkqq.sololeveling.telegram.bot.command.info

import com.sleepkqq.sololeveling.telegram.bot.grpc.client.UserApi
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserFeedbackService
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.telegram.telegrambots.meta.api.objects.message.Message

class UsersStatsCommandTest {

	private lateinit var userApi: UserApi
	private lateinit var userFeedbackService: UserFeedbackService
	private lateinit var command: UsersStatsCommand

	@BeforeEach
	fun setUp() {
		userApi = mock()
		userFeedbackService = mock()
		command = UsersStatsCommand(userApi, userFeedbackService)
	}

	@Test
	fun `command should return users_stats command string`() {
		// When/Then
		assertThat(command.command).isEqualTo("/users_stats")
	}

	@Test
	fun `visible should return false`() {
		// When/Then
		assertThat(command.visible).isFalse()
	}

	@Test
	fun `handle should return InfoCommandResult with user statistics`() {
		// Given
		val message = mock<Message>()
		val usersStats = mock<com.sleepkqq.sololeveling.grpc.UsersStats>()
		val feedbackCount = mock<com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedbackCount>()

		whenever(usersStats.total).thenReturn(1000L)
		whenever(usersStats.returning).thenReturn(800L)
		whenever(usersStats.todayTotal).thenReturn(100L)
		whenever(usersStats.todayReturning).thenReturn(80L)
		whenever(usersStats.todayNew).thenReturn(20L)
		whenever(usersStats.weekTotal).thenReturn(500L)
		whenever(usersStats.weekReturning).thenReturn(400L)
		whenever(usersStats.weekNew).thenReturn(100L)
		whenever(usersStats.monthTotal).thenReturn(900L)
		whenever(usersStats.monthReturning).thenReturn(700L)
		whenever(usersStats.monthNew).thenReturn(200L)

		whenever(feedbackCount.userCount).thenReturn(50L)
		whenever(feedbackCount.feedbackCount).thenReturn(75L)

		whenever(userApi.getUsersStats()).thenReturn(usersStats)
		whenever(userFeedbackService.getUserFeedbackCount()).thenReturn(feedbackCount)

		// When
		val result = command.handle(message)

		// Then
		assertThat(result).isNotNull
		assertThat(result.localizationCode).isEqualTo(LocalizationCode.CMD_USERS_STATS)
		assertThat(result.params).hasSize(13)
		assertThat(result.params).containsExactly(
			1000L, 800L, 100L, 80L, 20L, 500L, 400L, 100L, 900L, 700L, 200L, 50L, 75L
		)
		assertThat(result.keyboard).isNull()
	}

	@Test
	fun `handle should return correct parameters in order`() {
		// Given
		val message = mock<Message>()
		val usersStats = mock<com.sleepkqq.sololeveling.grpc.UsersStats>()
		val feedbackCount = mock<com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedbackCount>()

		whenever(usersStats.total).thenReturn(10L)
		whenever(usersStats.returning).thenReturn(20L)
		whenever(usersStats.todayTotal).thenReturn(30L)
		whenever(usersStats.todayReturning).thenReturn(40L)
		whenever(usersStats.todayNew).thenReturn(50L)
		whenever(usersStats.weekTotal).thenReturn(60L)
		whenever(usersStats.weekReturning).thenReturn(70L)
		whenever(usersStats.weekNew).thenReturn(80L)
		whenever(usersStats.monthTotal).thenReturn(90L)
		whenever(usersStats.monthReturning).thenReturn(100L)
		whenever(usersStats.monthNew).thenReturn(110L)
		whenever(feedbackCount.userCount).thenReturn(120L)
		whenever(feedbackCount.feedbackCount).thenReturn(130L)

		whenever(userApi.getUsersStats()).thenReturn(usersStats)
		whenever(userFeedbackService.getUserFeedbackCount()).thenReturn(feedbackCount)

		// When
		val result = command.handle(message)

		// Then - verify exact order
		assertThat(result.params[0]).isEqualTo(10L) // total
		assertThat(result.params[1]).isEqualTo(20L) // returning
		assertThat(result.params[2]).isEqualTo(30L) // todayTotal
		assertThat(result.params[3]).isEqualTo(40L) // todayReturning
		assertThat(result.params[4]).isEqualTo(50L) // todayNew
		assertThat(result.params[5]).isEqualTo(60L) // weekTotal
		assertThat(result.params[6]).isEqualTo(70L) // weekReturning
		assertThat(result.params[7]).isEqualTo(80L) // weekNew
		assertThat(result.params[8]).isEqualTo(90L) // monthTotal
		assertThat(result.params[9]).isEqualTo(100L) // monthReturning
		assertThat(result.params[10]).isEqualTo(110L) // monthNew
		assertThat(result.params[11]).isEqualTo(120L) // feedback userCount
		assertThat(result.params[12]).isEqualTo(130L) // feedback feedbackCount
	}

	@Test
	fun `handle should work with zero statistics`() {
		// Given
		val message = mock<Message>()
		val usersStats = mock<com.sleepkqq.sololeveling.grpc.UsersStats>()
		val feedbackCount = mock<com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedbackCount>()

		whenever(usersStats.total).thenReturn(0L)
		whenever(usersStats.returning).thenReturn(0L)
		whenever(usersStats.todayTotal).thenReturn(0L)
		whenever(usersStats.todayReturning).thenReturn(0L)
		whenever(usersStats.todayNew).thenReturn(0L)
		whenever(usersStats.weekTotal).thenReturn(0L)
		whenever(usersStats.weekReturning).thenReturn(0L)
		whenever(usersStats.weekNew).thenReturn(0L)
		whenever(usersStats.monthTotal).thenReturn(0L)
		whenever(usersStats.monthReturning).thenReturn(0L)
		whenever(usersStats.monthNew).thenReturn(0L)
		whenever(feedbackCount.userCount).thenReturn(0L)
		whenever(feedbackCount.feedbackCount).thenReturn(0L)

		whenever(userApi.getUsersStats()).thenReturn(usersStats)
		whenever(userFeedbackService.getUserFeedbackCount()).thenReturn(feedbackCount)

		// When
		val result = command.handle(message)

		// Then
		assertThat(result.params).hasSize(13)
		assertThat(result.params).allMatch { it == 0L }
	}
}