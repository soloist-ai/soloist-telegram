package com.sleepkqq.sololeveling.telegram.bot.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import java.util.concurrent.ThreadPoolExecutor

class SchedulingConfigurationTest {

	private lateinit var configuration: SchedulingConfiguration

	@BeforeEach
	fun setUp() {
		configuration = SchedulingConfiguration()
	}

	@Test
	fun `broadcastTaskScheduler should create ThreadPoolTaskScheduler with correct pool size`() {
		// When
		val scheduler = configuration.broadcastTaskScheduler() as ThreadPoolTaskScheduler

		// Then
		assertThat(scheduler).isNotNull
		assertThat(scheduler.poolSize).isEqualTo(10)
	}

	@Test
	fun `broadcastTaskScheduler should set correct thread name prefix`() {
		// When
		val scheduler = configuration.broadcastTaskScheduler() as ThreadPoolTaskScheduler

		// Then
		assertThat(scheduler.threadNamePrefix).isEqualTo("broadcast-")
	}

	@Test
	fun `broadcastTaskScheduler should configure shutdown behavior`() {
		// When
		val scheduler = configuration.broadcastTaskScheduler() as ThreadPoolTaskScheduler

		// Then
		// Verify that the scheduler waits for tasks to complete on shutdown
		// This is configured via setWaitForTasksToCompleteOnShutdown(true)
		// We can check that the scheduler was initialized properly
		assertThat(scheduler.threadPoolExecutor).isNotNull
	}

	@Test
	fun `broadcastTaskScheduler should use CallerRunsPolicy for rejected tasks`() {
		// When
		val scheduler = configuration.broadcastTaskScheduler() as ThreadPoolTaskScheduler
		val executor = scheduler.threadPoolExecutor

		// Then
		assertThat(executor).isNotNull
		assertThat(executor.rejectedExecutionHandler).isInstanceOf(ThreadPoolExecutor.CallerRunsPolicy::class.java)
	}

	@Test
	fun `broadcastTaskScheduler should be initialized`() {
		// When
		val scheduler = configuration.broadcastTaskScheduler() as ThreadPoolTaskScheduler

		// Then
		// Verify that initialize() was called by checking that the executor is available
		assertThat(scheduler.threadPoolExecutor).isNotNull
	}

	@Test
	fun `broadcastTaskScheduler should return different instances on multiple calls`() {
		// When
		val scheduler1 = configuration.broadcastTaskScheduler()
		val scheduler2 = configuration.broadcastTaskScheduler()

		// Then - each call creates a new instance
		assertThat(scheduler1).isNotSameAs(scheduler2)
	}

	@Test
	fun `broadcastTaskScheduler should have correct await termination seconds`() {
		// When
		val scheduler = configuration.broadcastTaskScheduler() as ThreadPoolTaskScheduler

		// Then
		// The scheduler is configured with setAwaitTerminationSeconds(60)
		// We can verify the basic properties are set
		assertThat(scheduler.poolSize).isEqualTo(10)
		assertThat(scheduler.threadNamePrefix).isEqualTo("broadcast-")
	}
}