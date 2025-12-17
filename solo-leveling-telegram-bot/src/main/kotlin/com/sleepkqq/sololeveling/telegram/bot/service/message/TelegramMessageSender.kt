package com.sleepkqq.sololeveling.telegram.bot.service.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.sleepkqq.sololeveling.telegram.bot.config.TelegramBotProperties
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import org.slf4j.LoggerFactory
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Service
class TelegramMessageSender(
	private val telegramBotProperties: TelegramBotProperties,
	private val restTemplate: RestTemplate,
	private val objectMapper: ObjectMapper
) {

	private companion object {
		// API methods
		const val SEND_MESSAGE = "sendMessage"
		const val SEND_PHOTO = "sendPhoto"

		// Rate limits
		const val RATE_LIMIT_GLOBAL = 30L
		const val RATE_LIMIT_PER_CHAT = 1L
		const val RATE_LIMIT_PERIOD_SECONDS = 1L

		// Request parameters
		const val PARAM_CHAT_ID = "chat_id"
		const val PARAM_PHOTO = "photo"
		const val PARAM_CAPTION = "caption"
		const val PARAM_PARSE_MODE = "parse_mode"
		const val PARAM_REPLY_MARKUP = "reply_markup"
	}

	private val log = LoggerFactory.getLogger(javaClass)

	private val globalBucket: Bucket = createBucket(RATE_LIMIT_GLOBAL)
	private val perChatBuckets = ConcurrentHashMap<Long, Bucket>()

	fun send(sendMessage: SendMessage) {
		val chatId = sendMessage.chatId.toLong()
		executeWithRateLimit(chatId) {
			sendRequest(SEND_MESSAGE, sendMessage)
		}
	}

	fun send(sendPhoto: SendPhoto) {
		val chatId = sendPhoto.chatId.toLong()
		executeWithRateLimit(chatId) {
			sendPhotoRequest(sendPhoto)
		}
	}

	private fun executeWithRateLimit(chatId: Long, action: () -> Unit) {
		val chatBucket = perChatBuckets.computeIfAbsent(chatId) {
			createBucket(RATE_LIMIT_PER_CHAT)
		}

		globalBucket.asBlocking().consume(1)
		chatBucket.asBlocking().consume(1)

		try {
			action()
			log.info("✓ Message sent to chatId=$chatId")

		} catch (ex: Exception) {
			log.error("✗ Failed to send message to chatId=$chatId: ${ex.message}", ex)
		}
	}

	private fun sendRequest(method: String, request: Any) {
		val url = buildApiUrl(method)
		val response = restTemplate.postForObject(url, request, String::class.java)
		log.debug("API response: $response")
	}

	private fun sendPhotoRequest(sendPhoto: SendPhoto) {
		val url = buildApiUrl(SEND_PHOTO)
		val requestEntity = buildPhotoRequest(sendPhoto)
		val response = restTemplate.postForObject(url, requestEntity, String::class.java)
		log.debug("API response: $response")
	}

	private fun buildPhotoRequest(sendPhoto: SendPhoto): HttpEntity<MultiValueMap<String, Any>> {
		val parts = LinkedMultiValueMap<String, Any>()

		parts.add(PARAM_CHAT_ID, sendPhoto.chatId)
		parts.addPhoto(sendPhoto.photo)
		parts.addIfPresent(PARAM_CAPTION, sendPhoto.caption)
		parts.addIfPresent(PARAM_PARSE_MODE, sendPhoto.parseMode)
		parts.addReplyMarkup(sendPhoto.replyMarkup)

		val headers = HttpHeaders().apply {
			contentType = MediaType.MULTIPART_FORM_DATA
		}

		return HttpEntity(parts, headers)
	}

	private fun MultiValueMap<String, Any>.addPhoto(photo: org.telegram.telegrambots.meta.api.objects.InputFile) {
		if (photo.isNew) {
			val resource = InputStreamResource(photo.newMediaStream)
			val headers = HttpHeaders().apply {
				contentType = MediaType.APPLICATION_OCTET_STREAM
				setContentDispositionFormData(PARAM_PHOTO, photo.mediaName)
			}
			add(PARAM_PHOTO, HttpEntity(resource, headers))
		} else {
			add(PARAM_PHOTO, photo.attachName ?: photo.mediaName)
		}
	}

	private fun MultiValueMap<String, Any>.addIfPresent(key: String, value: String?) {
		value?.let { add(key, it) }
	}

	private fun MultiValueMap<String, Any>.addReplyMarkup(replyMarkup: Any?) {
		replyMarkup?.let {
			add(PARAM_REPLY_MARKUP, objectMapper.writeValueAsString(it))
		}
	}

	private fun createBucket(capacity: Long): Bucket {
		return Bucket.builder()
			.addLimit(
				Bandwidth.builder()
					.capacity(capacity)
					.refillGreedy(capacity, Duration.ofSeconds(RATE_LIMIT_PERIOD_SECONDS))
					.build()
			)
			.build()
	}

	private fun buildApiUrl(method: String): String {
		return "${telegramBotProperties.api.url}${telegramBotProperties.token}/$method"
	}
}
