package com.sleepkqq.sololeveling.telegram.bot.service.user

import com.sleepkqq.sololeveling.telegram.bot.exception.ModelNotFoundException
import com.sleepkqq.sololeveling.telegram.model.entity.user.Fetchers
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSessionFetcher
import java.time.Instant

interface UserSessionService {

	fun find(
		id: Long,
		fetcher: UserSessionFetcher = Fetchers.USER_SESSION_FETCHER.allScalarFields()
	): UserSession?

	fun get(
		id: Long,
		fetcher: UserSessionFetcher = Fetchers.USER_SESSION_FETCHER.allScalarFields()
	): UserSession =
		find(id, fetcher) ?: throw ModelNotFoundException(UserSession::class, id)

	fun register(id: Long): UserSession

	fun update(session: UserSession, now: Instant = Instant.now()): UserSession
}