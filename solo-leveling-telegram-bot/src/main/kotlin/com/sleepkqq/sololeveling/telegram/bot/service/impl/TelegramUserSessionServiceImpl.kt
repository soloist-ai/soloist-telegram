package com.sleepkqq.sololeveling.telegram.bot.service.impl

import com.sleepkqq.sololeveling.telegram.bot.service.TelegramUserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.user.TelegramUserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.TelegramUserSessionFetcher
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.IdleState
import com.sleepkqq.sololeveling.telegram.model.repository.TelegramUserSessionRepository
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class TelegramUserSessionServiceImpl(
	private val telegramUserSessionRepository: TelegramUserSessionRepository
) : TelegramUserSessionService {

	@Transactional(readOnly = true)
	override fun find(id: Long, fetcher: TelegramUserSessionFetcher): TelegramUserSession? =
		telegramUserSessionRepository.findNullable(id, fetcher)

	@Transactional
	override fun register(id: Long): TelegramUserSession = telegramUserSessionRepository.save(
		Immutables.createTelegramUserSession {
			it.setId(id)
				.setState(IdleState())
		},
		SaveMode.INSERT_ONLY
	)

	@Transactional
	override fun update(session: TelegramUserSession, now: Instant): TelegramUserSession =
		telegramUserSessionRepository.save(
			Immutables.createTelegramUserSession {
				it.setUpdatedAt(now)
			},
			SaveMode.UPDATE_ONLY
		)
}
