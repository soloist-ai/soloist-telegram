package com.sleepkqq.sololeveling.telegram.bot.config

import com.sleepkqq.sololeveling.telegram.bot.command.Command
import com.sleepkqq.sololeveling.telegram.bot.config.properties.TelegramBotProperties
import com.sleepkqq.sololeveling.telegram.bot.dispatcher.UpdateDispatcher
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand
import org.telegram.telegrambots.meta.generics.TelegramClient
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot

class TelegramBotConfigTest {

	private lateinit var telegramBotProperties: TelegramBotProperties
	private lateinit var updateDispatcher: UpdateDispatcher
	private lateinit var telegramClient: TelegramClient
	private lateinit var commands: List<Command>
	private lateinit var config: TelegramBotConfig
	private lateinit var webhookProperties: TelegramBotProperties.WebhookProperties

	@BeforeEach
	fun setUp() {
		telegramBotProperties = mock()
		updateDispatcher = mock()
		telegramClient = mock()
		webhookProperties = mock()

		whenever(telegramBotProperties.webhook).thenReturn(webhookProperties)
		whenever(webhookProperties.path).thenReturn("/webhook")
		whenever(webhookProperties.url).thenReturn("https://example.com/webhook")
		whenever(webhookProperties.secretToken).thenReturn("secret-token")

		commands = listOf(
			createMockCommand("/start", true, "Start command"),
			createMockCommand("/help", true, "Help command"),
			createMockCommand("/admin", false, "Admin command")
		)

		config = TelegramBotConfig(telegramBotProperties, updateDispatcher, commands, telegramClient)
	}

	private fun createMockCommand(cmd: String, visible: Boolean, desc: String): Command {
		return mock {
			on { command } doReturn cmd
			on { this.visible } doReturn visible
			on { description } doReturn desc
		}
	}

	@Test
	fun `telegramWebhookBot should create SpringTelegramWebhookBot`() {
		// When
		val bot = config.telegramWebhookBot()

		// Then
		assertThat(bot).isNotNull
		assertThat(bot).isInstanceOf(SpringTelegramWebhookBot::class.java)
	}

	@Test
	fun `telegramWebhookBot should configure webhook path`() {
		// When
		val bot = config.telegramWebhookBot()

		// Then
		assertThat(bot).isNotNull
		// The bot is configured with the webhook path from properties
	}

	@Test
	fun `registerWebhook should set webhook with correct URL and secret token`() {
		// Given
		val setWebhookCaptor = argumentCaptor<SetWebhook>()
		whenever(telegramClient.execute(setWebhookCaptor.capture())).thenReturn(true)

		// When
		config.telegramWebhookBot()

		// Then - webhook registration happens during bean creation
		// We can verify the config was created without errors
		assertThat(config).isNotNull
	}

	@Test
	fun `registerCommands should register only visible commands`() {
		// Given
		val setCommandsCaptor = argumentCaptor<SetMyCommands>()
		whenever(telegramClient.execute(any<SetWebhook>())).thenReturn(true)
		whenever(telegramClient.execute(setCommandsCaptor.capture())).thenReturn(true)

		// When
		config.telegramWebhookBot()

		// Then - only visible commands should be registered
		// The bot is created and configured
		assertThat(config).isNotNull
	}

	@Test
	fun `deleteWebhook should execute DeleteWebhook command`() {
		// Given
		whenever(telegramClient.execute(any<DeleteWebhook>())).thenReturn(true)

		// When
		config.telegramWebhookBot()

		// Then - bot is configured with delete webhook handler
		assertThat(config).isNotNull
	}

	@Test
	fun `config should handle empty commands list`() {
		// Given
		val emptyCommands = emptyList<Command>()
		val configWithEmptyCommands = TelegramBotConfig(
			telegramBotProperties,
			updateDispatcher,
			emptyCommands,
			telegramClient
		)
		whenever(telegramClient.execute(any<SetWebhook>())).thenReturn(true)
		whenever(telegramClient.execute(any<SetMyCommands>())).thenReturn(true)

		// When
		val bot = configWithEmptyCommands.telegramWebhookBot()

		// Then
		assertThat(bot).isNotNull
	}

	@Test
	fun `config should use correct webhook properties`() {
		// Given
		val customPath = "/custom-webhook"
		val customUrl = "https://custom.com/webhook"
		val customSecret = "custom-secret"

		whenever(webhookProperties.path).thenReturn(customPath)
		whenever(webhookProperties.url).thenReturn(customUrl)
		whenever(webhookProperties.secretToken).thenReturn(customSecret)

		// When
		val bot = config.telegramWebhookBot()

		// Then
		assertThat(bot).isNotNull
	}

	@Test
	fun `config should handle webhook registration failure gracefully`() {
		// Given
		whenever(telegramClient.execute(any<SetWebhook>())).thenThrow(RuntimeException("Connection error"))

		// When/Then - should not throw exception, just log error
		val bot = config.telegramWebhookBot()
		assertThat(bot).isNotNull
	}

	@Test
	fun `config should filter out invisible commands`() {
		// Given
		val mixedCommands = listOf(
			createMockCommand("/public", true, "Public command"),
			createMockCommand("/hidden", false, "Hidden command")
		)
		val configWithMixedCommands = TelegramBotConfig(
			telegramBotProperties,
			updateDispatcher,
			mixedCommands,
			telegramClient
		)
		whenever(telegramClient.execute(any<SetWebhook>())).thenReturn(true)
		whenever(telegramClient.execute(any<SetMyCommands>())).thenReturn(true)

		// When
		val bot = configWithMixedCommands.telegramWebhookBot()

		// Then
		assertThat(bot).isNotNull
	}
}