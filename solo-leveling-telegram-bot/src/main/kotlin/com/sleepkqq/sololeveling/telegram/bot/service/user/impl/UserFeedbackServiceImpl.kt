package com.sleepkqq.sololeveling.telegram.bot.service.user.impl

import com.sleepkqq.sololeveling.telegram.bot.service.user.UserFeedbackService
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserService
import com.sleepkqq.sololeveling.telegram.model.entity.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedback
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedbackCount
import com.sleepkqq.sololeveling.telegram.model.repository.user.UserFeedbackRepository
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserFeedbackServiceImpl(
	private val userFeedbackRepository: UserFeedbackRepository,
	private val userService: UserService
) : UserFeedbackService {

	@Transactional(readOnly = true)
	override fun find(userId: Long): List<UserFeedback> = userFeedbackRepository.find(userId)

	@Transactional
	override fun create(userId: Long, message: String): UserFeedback {
		userService.register(userId)

		return userFeedbackRepository.save(
			Immutables.createUserFeedback {
				it.setId(UUID.randomUUID())
					.setMessage(message)
					.setUserId(userId)
			},
			SaveMode.INSERT_ONLY
		)
	}

	@Transactional(readOnly = true)
	override fun getUserFeedbackCount(): UserFeedbackCount =
		userFeedbackRepository.getUserFeedbackCount()
}