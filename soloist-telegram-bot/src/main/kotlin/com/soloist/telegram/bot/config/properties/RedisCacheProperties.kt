package com.soloist.telegram.bot.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "app.cache.redis")
data class RedisCacheProperties(
	val defaultTtl: Duration = Duration.ofHours(1),
	val caches: Map<CacheKey, CacheProperties> = emptyMap()
) {

	enum class CacheKey {
		USER_INFO
	}

	data class CacheProperties(
		val ttl: Duration,
		val nullable: Boolean = false
	)
}