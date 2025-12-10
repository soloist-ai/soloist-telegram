package com.sleepkqq.sololeveling.telegram.bot.service.user

import com.sleepkqq.sololeveling.telegram.bot.exception.ModelNotFoundException
import com.sleepkqq.sololeveling.telegram.model.entity.user.Fetchers
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSessionFetcher

interface UserSessionService {

	fun find(
		userId: Long,
		fetcher: UserSessionFetcher = Fetchers.USER_SESSION_FETCHER.allScalarFields()
	): UserSession?

	fun get(
		userId: Long,
		fetcher: UserSessionFetcher = Fetchers.USER_SESSION_FETCHER.allScalarFields()
	): UserSession =
		find(userId, fetcher) ?: throw ModelNotFoundException(UserSession::class, userId)

	fun register(userId: Long): UserSession
	fun update(session: UserSession): UserSession
	fun confirmInterruptState(userId: Long)
	fun cancelInterruptState(userId: Long)
	fun idleState(userId: Long)
}