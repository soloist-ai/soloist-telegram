package com.sleepkqq.sololeveling.telegram.bot.config.cache

import com.google.protobuf.MessageLite
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.SerializationException

class ProtobufRedisSerializer<T : MessageLite>(
	private val defaultInstance: T
) : RedisSerializer<T> {

	override fun serialize(t: T?): ByteArray? {
		return t?.toByteArray()
	}

	override fun deserialize(bytes: ByteArray?): T? {
		if (bytes == null || bytes.isEmpty()) return null
		return try {
			@Suppress("UNCHECKED_CAST")
			defaultInstance.parserForType.parseFrom(bytes) as T

		} catch (ex: Exception) {
			throw SerializationException("Error deserializing protobuf message", ex)
		}
	}
}
