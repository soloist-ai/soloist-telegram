package com.soloist.telegram.bot.service.message

import com.soloist.telegram.bot.config.properties.TelegramRateLimitProperties
import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce
import io.lettuce.core.RedisClient
import io.lettuce.core.codec.ByteArrayCodec
import org.springframework.stereotype.Component

@Component
class TelegramRateLimiter(
	private val properties: TelegramRateLimitProperties,
	redisClient: RedisClient
) {

	private val proxyManager = Bucket4jLettuce
		.casBasedBuilder(redisClient.connect(ByteArrayCodec.INSTANCE).async())
		.expirationAfterWrite(ExpirationAfterWriteStrategy.fixedTimeToLive(properties.bucketTtl))
		.build()

	private val globalBucket = proxyManager.builder()
		.build(properties.globalKey.toByteArray()) {
			buildBucketConfig(properties.global)
		}

	fun consume(chatId: String) {
		val chatBucket = proxyManager.builder()
			.build("${properties.chatKeyPrefix}$chatId".toByteArray()) {
				buildBucketConfig(properties.perChat)
			}

		globalBucket.asBlocking().consume(1)
		chatBucket.asBlocking().consume(1)
	}

	private fun buildBucketConfig(capacity: Long): BucketConfiguration = BucketConfiguration.builder()
		.addLimit { it.capacity(capacity).refillGreedy(capacity, properties.period) }
		.build()
}