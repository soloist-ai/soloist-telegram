package com.sleepkqq.sololeveling.telegram.bot.service.user.impl

import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSessionFetcher
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.IdleState
import com.sleepkqq.sololeveling.telegram.model.repository.user.UserSessionRepository
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class UserSessionServiceImpl(
	private val userSessionRepository: UserSessionRepository
) : UserSessionService {

	@Transactional(readOnly = true)
	override fun find(id: Long, fetcher: UserSessionFetcher): UserSession? =
		userSessionRepository.findNullable(id, fetcher)

	@Transactional
	override fun register(id: Long): UserSession = userSessionRepository.save(
		Immutables.createUserSession {
			it.setId(UUID.randomUUID())
				.setUser(Immutables.createUser { u -> u.setId(id) })
				.setState(IdleState())
		},
		SaveMode.UPSERT
	)

	@Transactional
	override fun update(session: UserSession, now: Instant): UserSession =
		userSessionRepository.save(
			Immutables.createUserSession(session) {
				it.setUpdatedAt(now)
			},
			SaveMode.UPDATE_ONLY
		)
}