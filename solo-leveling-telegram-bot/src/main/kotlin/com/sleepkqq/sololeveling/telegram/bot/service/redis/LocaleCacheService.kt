package com.sleepkqq.sololeveling.telegram.bot.service.redis

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.Locale
import java.util.concurrent.TimeUnit

@Service
class LocaleCacheService(
	private val redisTemplate: StringRedisTemplate
) {

	private val log = LoggerFactory.getLogger(javaClass)

	private companion object {
		const val LOCALE_KEY_PREFIX = "locale:user:"
		const val TTL_HOURS = 1L
	}

	fun getOrCacheLocale(userId: Long, localeProvider: () -> Locale): Locale {
		val redisKey = "$LOCALE_KEY_PREFIX$userId"

		val cachedLocaleTag = redisTemplate.opsForValue().get(redisKey)

		return if (cachedLocaleTag != null) {
			log.debug("Locale retrieved from cache | userId={}, locale={}", userId, cachedLocaleTag)
			Locale.forLanguageTag(cachedLocaleTag)
		} else {
			val locale = localeProvider()
			val localeTag = locale.toLanguageTag()

			redisTemplate.opsForValue().set(redisKey, localeTag, TTL_HOURS, TimeUnit.HOURS)
			log.info("Locale cached | userId={}, locale={}", userId, localeTag)

			locale
		}
	}

	fun updateLocale(userId: Long, locale: Locale) {
		val redisKey = "$LOCALE_KEY_PREFIX$userId"
		val localeTag = locale.toLanguageTag()

		redisTemplate.opsForValue().set(redisKey, localeTag, TTL_HOURS, TimeUnit.HOURS)
		log.info("Locale updated in cache | userId={}, locale={}", userId, localeTag)
	}

	fun evictLocale(userId: Long) {
		val redisKey = "$LOCALE_KEY_PREFIX$userId"
		redisTemplate.delete(redisKey)
		log.info("Locale evicted from cache | userId={}", userId)
	}
}