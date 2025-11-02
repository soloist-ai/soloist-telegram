package com.sleepkqq.sololeveling.telegram.bot.service

import com.sleepkqq.sololeveling.telegram.bot.exception.ModelNotFoundException
import com.sleepkqq.sololeveling.telegram.model.entity.user.Fetchers
import com.sleepkqq.sololeveling.telegram.model.entity.user.TelegramUserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.TelegramUserSessionFetcher
import java.time.Instant

interface TelegramUserSessionService {

	fun find(
		id: Long,
		fetcher: TelegramUserSessionFetcher = Fetchers.TELEGRAM_USER_SESSION_FETCHER.allScalarFields()
	): TelegramUserSession?

	fun get(
		id: Long,
		fetcher: TelegramUserSessionFetcher = Fetchers.TELEGRAM_USER_SESSION_FETCHER.allScalarFields()
	): TelegramUserSession =
		find(id, fetcher) ?: throw ModelNotFoundException(TelegramUserSession::class, id)

	fun register(id: Long): TelegramUserSession

	fun update(session: TelegramUserSession, now: Instant = Instant.now()): TelegramUserSession
}
