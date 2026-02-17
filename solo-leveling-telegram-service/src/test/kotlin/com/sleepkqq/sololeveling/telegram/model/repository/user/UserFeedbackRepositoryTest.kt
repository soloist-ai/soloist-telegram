package com.sleepkqq.sololeveling.telegram.model.repository.user

import com.sleepkqq.sololeveling.telegram.BaseTestClass
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserService
import com.sleepkqq.sololeveling.telegram.model.entity.Immutables
import org.assertj.core.api.Assertions.assertThat
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant

class UserFeedbackRepositoryTest : BaseTestClass() {

	@Autowired
	private lateinit var userFeedbackRepository: UserFeedbackRepository

	@Autowired
	private lateinit var userService: UserService

	@Test
	fun `getUserFeedbackCount returns correct statistics`() {
		// Given: создаём 3-х пользователей
		val now = Instant.now()

		userService.register(1001)
		userService.register(1002)
		userService.register(1003)

		// user1: 2 фидбека
		val f1 = Immutables.createUserFeedback {
			it.setMessage("u1 feedback 1")
			it.setUserId(1001)
			it.setCreatedAt(now)
		}
		val f2 = Immutables.createUserFeedback {
			it.setMessage("u1 feedback 2")
			it.setUserId(1001)
			it.setCreatedAt(now)
		}

		// user2: 1 фидбек
		val f3 = Immutables.createUserFeedback {
			it.setMessage("u2 feedback 1")
			it.setUserId(1002)
			it.setCreatedAt(now)
		}

		// user3: 0 фидбеков

		userFeedbackRepository.save(f1, SaveMode.INSERT_ONLY)
		userFeedbackRepository.save(f2, SaveMode.INSERT_ONLY)
		userFeedbackRepository.save(f3, SaveMode.INSERT_ONLY)

		// When
		val stats = userFeedbackRepository.getUserFeedbackCount()

		// Then
		// Пользователей, оставивших хотя бы один фидбек: user1, user2
		assertThat(stats.userCount).isEqualTo(2)
		// Всего фидбеков: 3
		assertThat(stats.feedbackCount).isEqualTo(3)
	}

	@Test
	fun `getUserFeedbackCount returns zeros when no feedbacks`() {
		// Given: база пустая или есть пользователи без фидбеков

		// When
		val stats = userFeedbackRepository.getUserFeedbackCount()

		// Then
		assertThat(stats.userCount).isEqualTo(0)
		assertThat(stats.feedbackCount).isEqualTo(0)
	}
}
