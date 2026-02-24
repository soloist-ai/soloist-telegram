package com.soloist.telegram.bot.service.broadcast

import com.soloist.telegram.bot.exception.ModelNotFoundException
import com.soloist.telegram.model.entity.broadcast.ScheduledBroadcast
import com.soloist.telegram.model.entity.broadcast.enums.BroadcastStatus
import com.soloist.telegram.model.entity.user.state.newsletter.NewsletterConfirmationState
import org.babyfish.jimmer.View
import java.util.UUID
import kotlin.reflect.KClass

interface ScheduledBroadcastService {

	fun <V : View<ScheduledBroadcast>> findView(id: UUID, viewType: KClass<V>): V?
	fun <V : View<ScheduledBroadcast>> getView(id: UUID, viewType: KClass<V>): V =
		findView(id, viewType) ?: throw ModelNotFoundException(ScheduledBroadcast::class, id)

	fun insert(state: NewsletterConfirmationState): ScheduledBroadcast
	fun update(broadcast: ScheduledBroadcast): ScheduledBroadcast
	fun updateStatus(broadcast: ScheduledBroadcast, status: BroadcastStatus): ScheduledBroadcast
	fun <V : View<ScheduledBroadcast>> findByStatus(
		status: BroadcastStatus,
		viewType: KClass<V>
	): List<V>
}