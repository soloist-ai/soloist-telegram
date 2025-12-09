package com.sleepkqq.sololeveling.telegram.bot.service.user.impl

import com.sleepkqq.sololeveling.telegram.bot.service.user.UserFeedbackService
import com.sleepkqq.sololeveling.telegram.model.entity.user.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedback
import com.sleepkqq.sololeveling.telegram.model.repository.user.UserFeedbackRepository
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserFeedbackServiceImpl(
	private val userFeedbackRepository: UserFeedbackRepository
) : UserFeedbackService {

	override fun find(userId: Long): List<UserFeedback> = userFeedbackRepository.find(userId)

	override fun create(userId: Long, message: String): UserFeedback =
		userFeedbackRepository.save(
			Immutables.createUserFeedback {
				it.setId(UUID.randomUUID())
					.setMessage(message)
					.setUser(Immutables.createUser { u -> u.setId(userId) })
			},
			SaveMode.UPSERT
		)
}