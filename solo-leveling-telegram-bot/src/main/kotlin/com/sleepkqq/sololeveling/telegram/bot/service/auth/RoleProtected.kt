package com.sleepkqq.sololeveling.telegram.bot.service.auth

import com.sleepkqq.sololeveling.telegram.bot.model.UserRole

interface RoleProtected {

	val requiredRole: UserRole
		get() = UserRole.USER
}
