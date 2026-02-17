package com.sleepkqq.sololeveling.telegram.bot.service.user.impl

import com.sleepkqq.sololeveling.telegram.bot.service.user.UserService
import com.sleepkqq.sololeveling.telegram.model.entity.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.user.User
import com.sleepkqq.sololeveling.telegram.model.repository.user.UserRepository
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
	private val userRepository: UserRepository
) : UserService {

	@Transactional
	override fun register(id: Long): User =
		userRepository.save(
			Immutables.createUser { it.setId(id).setVersion(0) },
			SaveMode.INSERT_IF_ABSENT
		)
}