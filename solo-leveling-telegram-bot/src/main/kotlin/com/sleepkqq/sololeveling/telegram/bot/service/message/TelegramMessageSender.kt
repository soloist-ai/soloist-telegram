package com.sleepkqq.sololeveling.telegram.bot.service.message

import com.sleepkqq.sololeveling.telegram.bot.config.properties.TelegramRateLimitProperties
import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce
import io.lettuce.core.RedisClient
import io.lettuce.core.codec.ByteArrayCodec
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.generics.TelegramClient

@Service
class TelegramMessageSender(
	private val telegramClient: TelegramClient,
	telegramRateLimitProperties: TelegramRateLimitProperties,
	redisClient: RedisClient
) {

	private val rateLimitConfig = telegramRateLimitProperties.sendMessage

	private val globalBucketConfig = buildBucketConfig(rateLimitConfig.global)
	private val perChatBucketConfig = buildBucketConfig(rateLimitConfig.perChat)

	private val proxyManager = Bucket4jLettuce
		.casBasedBuilder(redisClient.connect(ByteArrayCodec.INSTANCE).async())
		.expirationAfterWrite(ExpirationAfterWriteStrategy.fixedTimeToLive(rateLimitConfig.bucketTtl))
		.build()

	private val globalBucket = proxyManager.builder()
		.build(rateLimitConfig.globalKey.toByteArray()) { globalBucketConfig }

	fun send(sendMessage: SendMessage) = executeWithRateLimit(sendMessage.chatId)
	{ telegramClient.execute(sendMessage) }

	fun send(sendPhoto: SendPhoto) = executeWithRateLimit(sendPhoto.chatId)
	{ telegramClient.execute(sendPhoto) }

	private fun executeWithRateLimit(chatId: String, action: () -> Unit) {
		val chatBucket = proxyManager.builder()
			.build("${rateLimitConfig.chatKeyPrefix}$chatId".toByteArray()) { perChatBucketConfig }

		globalBucket.asBlocking().consume(1)
		chatBucket.asBlocking().consume(1)
		action()
	}

	private fun buildBucketConfig(capacity: Long): BucketConfiguration = BucketConfiguration.builder()
		.addLimit { it.capacity(capacity).refillGreedy(capacity, rateLimitConfig.period) }
		.build()
}
