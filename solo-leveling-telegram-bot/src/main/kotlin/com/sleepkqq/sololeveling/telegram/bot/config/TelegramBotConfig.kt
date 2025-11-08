package com.sleepkqq.sololeveling.telegram.bot.config

import com.sleepkqq.sololeveling.telegram.bot.dispatcher.UpdateDispatcher
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot

@Configuration
@EnableConfigurationProperties(TelegramBotProperties::class)
class TelegramBotConfig(
	private val telegramBotProperties: TelegramBotProperties,
	private val updateDispatcher: UpdateDispatcher,
	private val restTemplate: RestTemplate
) {

	private val log = LoggerFactory.getLogger(javaClass)

	@Bean
	fun telegramWebhookBot(): SpringTelegramWebhookBot {
		log.info("Initializing Telegram Webhook Bot")

		return SpringTelegramWebhookBot.builder()
			.botPath(telegramBotProperties.webhook.path)
			.updateHandler { updateDispatcher.dispatch(it) }
			.setWebhook { registerWebhook() }
			.deleteWebhook { deleteWebhook() }
			.build()
	}

	private fun registerWebhook() {
		try {
			val response = restTemplate.postForObject(
				"${telegramBotProperties.api.url}${telegramBotProperties.token}/setWebhook",
				SetWebhook.builder()
					.url(telegramBotProperties.webhook.url)
					.secretToken(telegramBotProperties.webhook.secretToken)
					.build(),
				String::class.java
			)
			log.info("Webhook registered: ${telegramBotProperties.webhook.url}, response=$response")

		} catch (ex: Exception) {
			log.error("Error registering webhook: ${ex.message}", ex)
		}
	}

	private fun deleteWebhook() {
		try {
			val response = restTemplate.postForObject(
				"${telegramBotProperties.api.url}${telegramBotProperties.token}/deleteWebhook",
				null,
				String::class.java
			)
			log.info("Webhook deleted, response=$response")

		} catch (ex: Exception) {
			log.error("Error deleting webhook: ${ex.message}", ex)
		}
	}
}
