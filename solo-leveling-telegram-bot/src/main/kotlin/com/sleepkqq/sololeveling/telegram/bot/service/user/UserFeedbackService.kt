package com.sleepkqq.sololeveling.telegram.bot.service.user

import com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedback
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedbackCount

interface UserFeedbackService {

	fun find(userId: Long): List<UserFeedback>
	fun create(userId: Long, message: String): UserFeedback
	fun getUserFeedbackCount(): UserFeedbackCount
}