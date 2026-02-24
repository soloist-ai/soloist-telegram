package com.soloist.telegram.bot.service.broadcast.impl

import com.soloist.telegram.bot.service.broadcast.ScheduledBroadcastService
import com.soloist.telegram.bot.service.localization.LocalizationMessageService
import com.soloist.telegram.model.entity.Immutables
import com.soloist.telegram.model.entity.broadcast.ScheduledBroadcast
import com.soloist.telegram.model.entity.broadcast.dto.ScheduledBroadcastView
import com.soloist.telegram.model.entity.broadcast.enums.BroadcastStatus
import com.soloist.telegram.model.entity.user.state.newsletter.NewsletterConfirmationState
import com.soloist.telegram.model.repository.broadcast.ScheduledBroadcastRepository
import org.babyfish.jimmer.View
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.reflect.KClass

@Service
class ScheduledBroadcastServiceImpl(
	private val scheduledBroadcastRepository: ScheduledBroadcastRepository,
	private val localizationMessageService: LocalizationMessageService,
	private val broadcastScheduler: BroadcastScheduler
) : ScheduledBroadcastService {

	@EventListener(ApplicationReadyEvent::class)
	fun restoreOnStartup() {
		findByStatus(BroadcastStatus.PENDING, ScheduledBroadcastView::class)
			.forEach { broadcastScheduler.schedule(it) }
	}

	@Transactional(readOnly = true)
	override fun <V : View<ScheduledBroadcast>> findView(id: UUID, viewType: KClass<V>): V? =
		scheduledBroadcastRepository.findView(id, viewType.java)

	@Transactional
	override fun insert(state: NewsletterConfirmationState): ScheduledBroadcast {
		val broadcast = initialize(state)
		val result = scheduledBroadcastRepository.save(broadcast, SaveMode.INSERT_ONLY)
		broadcastScheduler.schedule(ScheduledBroadcastView(result))
		return result
	}

	private fun initialize(state: NewsletterConfirmationState): ScheduledBroadcast =
		Immutables.createScheduledBroadcast {
			it.setId(UUID.randomUUID())
				.setName(state.name)
				.setFileId(state.fileId)
				.setScheduledAt(state.scheduledAt)
				.setStatus(BroadcastStatus.PENDING)
				.setMessages(state.localizations.map(localizationMessageService::initialize))
		}

	@Transactional
	override fun update(broadcast: ScheduledBroadcast): ScheduledBroadcast =
		scheduledBroadcastRepository.save(broadcast, SaveMode.UPDATE_ONLY)

	@Transactional
	override fun updateStatus(
		broadcast: ScheduledBroadcast,
		status: BroadcastStatus
	): ScheduledBroadcast = update(Immutables.createScheduledBroadcast(broadcast) {
		it.setStatus(status)
	})

	@Transactional(readOnly = true)
	override fun <V : View<ScheduledBroadcast>> findByStatus(
		status: BroadcastStatus,
		viewType: KClass<V>
	): List<V> = scheduledBroadcastRepository.findByStatus(status, viewType.java)
}
