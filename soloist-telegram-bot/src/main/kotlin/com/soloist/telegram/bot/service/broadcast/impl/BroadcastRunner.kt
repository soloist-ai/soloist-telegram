package com.soloist.telegram.bot.service.broadcast.impl

import com.soloist.telegram.bot.event.RunBroadcastEvent
import com.soloist.telegram.bot.grpc.client.UserApi
import com.soloist.telegram.bot.service.broadcast.ScheduledBroadcastService
import com.soloist.telegram.model.entity.Immutables
import com.soloist.telegram.model.entity.broadcast.enums.BroadcastStatus
import org.babyfish.jimmer.sql.exception.SaveException
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class BroadcastRunner(
	private val broadcastExecutor: BroadcastExecutor,
	private val scheduledBroadcastService: ScheduledBroadcastService,
	private val userApi: UserApi
) {

	private companion object {
		const val PAGE_SIZE = 100
	}

	private val log = LoggerFactory.getLogger(javaClass)

	@EventListener
	fun listen(event: RunBroadcastEvent) {
		val broadcast = event.broadcast
		if (broadcast.status != BroadcastStatus.PENDING) return

		val inProgressBroadcast = try {
			scheduledBroadcastService.updateStatus(
				broadcast.toEntity(),
				BroadcastStatus.IN_PROGRESS
			)
		} catch (_: SaveException.OptimisticLockError) {
			log.info("Broadcast {} already claimed by concurrent event, skipping", broadcast.id)
			return
		}

		var currentPage = 0
		var total = 0
		var totalSuccess = 0
		var totalFailed = 0
		var status = BroadcastStatus.FAILED

		try {
			do {
				val response = userApi.getUsers(currentPage, PAGE_SIZE)

				val result = broadcastExecutor.execute(broadcast, response.usersList)
				total += result.total
				totalSuccess += result.totalSuccess
				totalFailed += result.totalFailed

				currentPage++
			} while (response.paging.hasMore)

			status = BroadcastStatus.COMPLETED

			log.info(
				"Broadcast {} completed: total={} success={}, failed={}",
				broadcast.id, total, totalSuccess, totalFailed
			)

		} catch (e: Exception) {
			log.error(
				"Broadcast {} failed at page {}: total={} success={} failed={}",
				broadcast.id, currentPage, total, totalSuccess, totalFailed, e
			)

		} finally {
			scheduledBroadcastService.update(
				Immutables.createScheduledBroadcast(inProgressBroadcast) {
					it.setStatus(status)
						.setTotal(total)
						.setTotalSuccess(totalSuccess)
						.setTotalFailed(totalFailed)
				}
			)
		}
	}
}
