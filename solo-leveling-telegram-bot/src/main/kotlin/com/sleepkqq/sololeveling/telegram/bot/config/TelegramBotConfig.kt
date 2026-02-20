package com.sleepkqq.sololeveling.telegram.bot.config

import com.sleepkqq.sololeveling.telegram.bot.command.Command
import com.sleepkqq.sololeveling.telegram.bot.config.properties.TelegramBotProperties
import com.sleepkqq.sololeveling.telegram.bot.dispatcher.UpdateDispatcher
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand
import org.telegram.telegrambots.meta.generics.TelegramClient
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot

@Configuration
class TelegramBotConfig(
	private val telegramBotProperties: TelegramBotProperties,
	private val updateDispatcher: UpdateDispatcher,
	private val commands: List<Command>,
	private val telegramClient: TelegramClient
) {

	private val log = LoggerFactory.getLogger(javaClass)

	@Bean
	fun telegramWebhookBot(): SpringTelegramWebhookBot =
		SpringTelegramWebhookBot.builder()
			.botPath(telegramBotProperties.webhook.path)
			.updateHandler { updateDispatcher.dispatch(it) }
			.setWebhook { registerWebhook() }
			.deleteWebhook { deleteWebhook() }
			.build()

	private fun registerWebhook() {
		try {
			telegramClient.execute(
				SetWebhook.builder()
					.url(telegramBotProperties.webhook.url)
					.secretToken(telegramBotProperties.webhook.secretToken)
					.build()
			)
			log.info("Webhook registered: ${telegramBotProperties.webhook.url}")
			registerCommands()

		} catch (ex: Exception) {
			log.error("Error registering webhook: ${ex.message}", ex)
		}
	}

	private fun deleteWebhook() {
		try {
			telegramClient.execute(DeleteWebhook.builder().build())
			log.info("Webhook deleted")

		} catch (ex: Exception) {
			log.error("Error deleting webhook: ${ex.message}", ex)
		}
	}

	private fun registerCommands() {
		val botCommands = commands.filter { it.visible }
			.map { BotCommand(it.command, it.description) }

		telegramClient.execute(SetMyCommands.builder().commands(botCommands).build())
		log.info("Commands registered: ${botCommands.map { it.command }}")
	}
}
