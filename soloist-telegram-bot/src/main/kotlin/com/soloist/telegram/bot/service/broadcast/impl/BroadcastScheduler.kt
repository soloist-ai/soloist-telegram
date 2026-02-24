package com.soloist.telegram.bot.service.broadcast.impl

import com.soloist.telegram.bot.event.RunBroadcastEvent
import com.soloist.telegram.model.entity.broadcast.dto.ScheduledBroadcastView
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ScheduledFuture

@Component
class BroadcastScheduler(
	private val broadcastTaskScheduler: TaskScheduler,
	private val eventPublisher: ApplicationEventPublisher
) {

	private val log = LoggerFactory.getLogger(javaClass)
	private val scheduledTasks = ConcurrentHashMap<UUID, ScheduledFuture<*>>()

	fun schedule(broadcast: ScheduledBroadcastView) {
		val effectiveTime = if (broadcast.scheduledAt.isBefore(Instant.now())) {
			log.warn("Broadcast {} is overdue, executing immediately", broadcast.id)
			Instant.now()
		} else {
			broadcast.scheduledAt
		}

		val future = broadcastTaskScheduler.schedule(
			{ eventPublisher.publishEvent(RunBroadcastEvent(broadcast)) },
			effectiveTime
		)

		scheduledTasks[broadcast.id] = future
		log.info("Scheduled broadcast {} at {}", broadcast.id, effectiveTime)
	}

	fun cancel(id: UUID): Boolean {
		val future = scheduledTasks.remove(id) ?: return false
		return future.cancel(false).also { cancelled ->
			if (cancelled) log.info("Cancelled broadcast {}", id)
		}
	}
}
