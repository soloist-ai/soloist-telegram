package com.sleepkqq.sololeveling.telegram.bot.config.cache

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "app.cache.redis")
data class RedisCacheProperties(
	val defaultTtl: Duration = Duration.ofHours(1),
	val caches: Map<String, CacheProperties> = emptyMap()
) {

	data class CacheProperties(
		val ttl: Duration,
		val nullable: Boolean = false
	)
}
