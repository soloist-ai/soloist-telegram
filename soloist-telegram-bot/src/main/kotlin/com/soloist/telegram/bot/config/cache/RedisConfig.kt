package com.soloist.telegram.bot.config.cache

import com.google.protobuf.MessageLite
import com.soloist.proto.user.GetUserAdditionalInfoResponse
import com.soloist.telegram.bot.config.properties.RedisCacheProperties
import io.lettuce.core.RedisClient
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@EnableCaching
@Configuration
class RedisConfig(
	private val cacheProperties: RedisCacheProperties
) {

	@Bean
	fun redisCacheManagerBuilderCustomizer() = RedisCacheManagerBuilderCustomizer { builder ->
		builder.cacheDefaults(createDefaultCacheConfig())

		val configMap = cacheProperties.caches.entries
			.associate { (key, config) ->
				key.name to when (key) {
					RedisCacheProperties.CacheKey.USER_INFO -> createProtoCacheConfig(
						ttl = config.ttl,
						nullable = config.nullable,
						serializer = ProtobufRedisSerializer(GetUserAdditionalInfoResponse.getDefaultInstance())
					)
				}
			}

		builder.withInitialCacheConfigurations(configMap)
	}

	@Bean
	fun redisClient(factory: LettuceConnectionFactory): RedisClient =
		factory.nativeClient as RedisClient

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

	private fun <T : MessageLite> createProtoCacheConfig(
		ttl: Duration,
		nullable: Boolean,
		serializer: RedisSerializer<T>
	): RedisCacheConfiguration {
		var config = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(ttl)
			.serializeKeysWith(
				RedisSerializationContext.SerializationPair.fromSerializer(
					StringRedisSerializer()
				)
			)
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(serializer)
			)

		if (!nullable) {
			config = config.disableCachingNullValues()
		}

		return config
	}
}
