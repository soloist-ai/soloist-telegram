package com.sleepkqq.sololeveling.telegram.bot.config

import com.sleepkqq.sololeveling.telegram.bot.config.properties.TelegramBotProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient

class TelegramClientConfigTest {

	private lateinit var telegramBotProperties: TelegramBotProperties
	private lateinit var config: TelegramClientConfig

	@BeforeEach
	fun setUp() {
		telegramBotProperties = mock()
		config = TelegramClientConfig(telegramBotProperties)
	}

	@Test
	fun `telegramClient should create OkHttpTelegramClient with bot token`() {
		// Given
		val botToken = "test-bot-token-123456"
		whenever(telegramBotProperties.token).thenReturn(botToken)

		// When
		val client = config.telegramClient()

		// Then
		assertThat(client).isNotNull
		assertThat(client).isInstanceOf(OkHttpTelegramClient::class.java)
	}

	@Test
	fun `telegramClient should work with different tokens`() {
		// Given
		val botToken1 = "token-1"
		val botToken2 = "token-2"

		whenever(telegramBotProperties.token).thenReturn(botToken1)
		val client1 = config.telegramClient()

		whenever(telegramBotProperties.token).thenReturn(botToken2)
		val client2 = config.telegramClient()

		// Then
		assertThat(client1).isNotNull
		assertThat(client2).isNotNull
		// Each call creates a new client
	}

	@Test
	fun `telegramClient should handle empty token`() {
		// Given
		whenever(telegramBotProperties.token).thenReturn("")

		// When
		val client = config.telegramClient()

		// Then
		assertThat(client).isNotNull
	}

	@Test
	fun `telegramClient should return OkHttpTelegramClient type`() {
		// Given
		whenever(telegramBotProperties.token).thenReturn("test-token")

		// When
		val client = config.telegramClient()

		// Then
		assertThat(client).isInstanceOf(OkHttpTelegramClient::class.java)
	}
}