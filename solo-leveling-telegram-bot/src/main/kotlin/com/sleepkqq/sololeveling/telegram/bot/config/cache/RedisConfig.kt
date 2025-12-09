package com.sleepkqq.sololeveling.telegram.bot.config

import com.sleepkqq.sololeveling.telegram.bot.config.cache.RedisCacheProperties
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
@EnableConfigurationProperties(RedisCacheProperties::class)
class RedisConfig(
	private val cacheProperties: RedisCacheProperties
) {

	@Bean
	fun redisCacheManagerBuilderCustomizer() = RedisCacheManagerBuilderCustomizer { builder ->
		builder.cacheDefaults(createDefaultCacheConfig())

		val configMap = cacheProperties.caches.mapValues { (_, config) ->
			createCacheConfig(config.ttl, config.nullable)
		}

		builder.withInitialCacheConfigurations(configMap)
	}

	private fun createDefaultCacheConfig(): RedisCacheConfiguration {
		return RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(cacheProperties.defaultTtl)
			.disableCachingNullValues()
			.serializeKeysWith(
				RedisSerializationContext.SerializationPair.fromSerializer(
					StringRedisSerializer()
				)
			)
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(
					GenericJackson2JsonRedisSerializer()
				)
			)
	}

	private fun createCacheConfig(ttl: Duration, nullable: Boolean): RedisCacheConfiguration {
		var config = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(ttl)
			.serializeKeysWith(
				RedisSerializationContext.SerializationPair.fromSerializer(
					StringRedisSerializer()
				)
			)
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(
					GenericJackson2JsonRedisSerializer()
				)
			)

		if (!nullable) {
			config = config.disableCachingNullValues()
		}

		return config
	}
}
