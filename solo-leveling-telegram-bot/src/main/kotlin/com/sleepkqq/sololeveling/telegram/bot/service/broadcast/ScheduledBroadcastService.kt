package com.sleepkqq.sololeveling.telegram.bot.service.broadcast

import com.sleepkqq.sololeveling.telegram.model.entity.broadcast.ScheduledBroadcast
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter.NewsletterConfirmationState
import java.util.UUID

interface ScheduledBroadcastService {

	fun initialize(state: NewsletterConfirmationState): ScheduledBroadcast
	fun cancel(id: UUID)
}