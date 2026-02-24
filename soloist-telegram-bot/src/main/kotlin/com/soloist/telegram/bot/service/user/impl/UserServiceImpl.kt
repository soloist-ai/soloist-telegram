package com.soloist.telegram.bot.service.user.impl

import com.soloist.telegram.bot.service.user.UserService
import com.soloist.telegram.model.entity.Immutables
import com.soloist.telegram.model.entity.user.User
import com.soloist.telegram.model.repository.user.UserRepository
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