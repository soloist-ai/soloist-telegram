package com.soloist.telegram.bot.service.user

import com.soloist.telegram.model.entity.user.User

interface UserService {

	fun register(id: Long): User
}