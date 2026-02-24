package com.soloist.telegram.bot.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.schedule.broadcast.task")
data class ScheduledBroadcastTaskProperties(
	val poolSize: Int,
	val awaitTerminationSeconds: Int
)
