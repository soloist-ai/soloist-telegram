package com.soloist.telegram.bot.service.user

import com.soloist.telegram.model.entity.user.UserFeedback
import com.soloist.telegram.model.entity.user.UserFeedbackCount

interface UserFeedbackService {

	fun find(userId: Long): List<UserFeedback>
	fun create(userId: Long, message: String): UserFeedback
	fun getUserFeedbackCount(): UserFeedbackCount
}