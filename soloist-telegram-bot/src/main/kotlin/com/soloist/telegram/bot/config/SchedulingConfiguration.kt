package com.soloist.telegram.bot.config

import com.soloist.telegram.bot.config.properties.ScheduledBroadcastTaskProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import java.util.concurrent.ThreadPoolExecutor

@Configuration
@EnableScheduling
class SchedulingConfiguration {

	@Bean
	fun broadcastTaskScheduler(properties: ScheduledBroadcastTaskProperties): TaskScheduler =
		ThreadPoolTaskScheduler().apply {
			poolSize = properties.poolSize
			setThreadNamePrefix("broadcast-")
			setAwaitTerminationSeconds(properties.awaitTerminationSeconds)
			setWaitForTasksToCompleteOnShutdown(true)
			setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
			initialize()
		}
}
