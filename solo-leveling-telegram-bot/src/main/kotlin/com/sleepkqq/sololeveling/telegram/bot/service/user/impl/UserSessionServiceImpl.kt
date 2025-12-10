package com.sleepkqq.sololeveling.telegram.bot.service.user.impl

import com.sleepkqq.sololeveling.telegram.bot.service.user.UserService
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSessionFetcher
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.IdleState
import com.sleepkqq.sololeveling.telegram.model.repository.user.UserSessionRepository
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserSessionServiceImpl(
	private val userSessionRepository: UserSessionRepository,
	private val userService: UserService
) : UserSessionService {

	@Transactional(readOnly = true)
	override fun find(userId: Long, fetcher: UserSessionFetcher): UserSession? =
		userSessionRepository.findNullable(userId, fetcher)

	@Transactional
	override fun register(userId: Long): UserSession {
		userService.register(userId)

		return userSessionRepository.save(
			Immutables.createUserSession {
				it.setId(UUID.randomUUID())
					.setUserId(userId)
					.setState(IdleState())
			},
			SaveMode.INSERT_ONLY
		)
	}

	@Transactional
	override fun update(session: UserSession): UserSession =
		userSessionRepository.save(session, SaveMode.UPDATE_ONLY)

	@Transactional
	override fun confirmInterruptState(userId: Long) =
		userSessionRepository.confirmInterruptState(userId)

	@Transactional
	override fun cancelInterruptState(userId: Long) =
		userSessionRepository.cancelInterruptState(userId)

	@Transactional
	override fun idleState(userId: Long) {
		userSessionRepository.updateState(userId, IdleState())
	}
}