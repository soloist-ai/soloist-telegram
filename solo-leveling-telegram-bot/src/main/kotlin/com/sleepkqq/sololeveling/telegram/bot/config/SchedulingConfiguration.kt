package com.sleepkqq.sololeveling.telegram.bot.config

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
	fun broadcastTaskScheduler(): TaskScheduler = ThreadPoolTaskScheduler().apply {
		poolSize = 10
		setThreadNamePrefix("broadcast-")
		setAwaitTerminationSeconds(60)
		setWaitForTasksToCompleteOnShutdown(true)
		setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
		initialize()
	}
}
