package com.sleepkqq.sololeveling.telegram.bot.service.broadcast.impl

import com.sleepkqq.sololeveling.telegram.bot.service.broadcast.ScheduledBroadcastService
import com.sleepkqq.sololeveling.telegram.bot.service.localization.LocalizationMessageService
import com.sleepkqq.sololeveling.telegram.model.entity.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.broadcast.ScheduledBroadcast
import com.sleepkqq.sololeveling.telegram.model.entity.broadcast.enums.BroadcastStatus
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter.NewsletterConfirmationState
import com.sleepkqq.sololeveling.telegram.model.repository.broadcast.ScheduledBroadcastRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ScheduledBroadcastServiceImpl(
	private val scheduledBroadcastRepository: ScheduledBroadcastRepository,
	private val localizationMessageService: LocalizationMessageService
) : ScheduledBroadcastService {

	override fun initialize(state: NewsletterConfirmationState): ScheduledBroadcast =
		Immutables.createScheduledBroadcast {
			it.setId(UUID.randomUUID())
				.setScheduledAt(state.scheduledAt)
				.setStatus(BroadcastStatus.PENDING)
				.setMessages(state.localizations.map(localizationMessageService::initialize))
		}

	override fun cancel(id: UUID) {
		TODO("Not yet implemented")
	}
}