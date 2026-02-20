package com.sleepkqq.sololeveling.telegram.bot.command.info

import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.telegram.telegrambots.meta.api.objects.message.Message

class StartCommandTest {

	private lateinit var command: StartCommand

	@BeforeEach
	fun setUp() {
		command = StartCommand()
	}

	@Test
	fun `command should return start command string`() {
		// When/Then
		assertThat(command.command).isEqualTo("/start")
	}

	@Test
	fun `visible should return true`() {
		// When/Then
		assertThat(command.visible).isTrue()
	}

	@Test
	fun `description should return start description`() {
		// When/Then
		assertThat(command.description).isEqualTo("Start working with the bot")
	}

	@Test
	fun `handle should return InfoCommandResult with CMD_START and MINI_APP_LINK keyboard`() {
		// Given
		val message = mock<Message>()

		// When
		val result = command.handle(message)

		// Then
		assertThat(result).isNotNull
		assertThat(result.localizationCode).isEqualTo(LocalizationCode.CMD_START)
		assertThat(result.params).isEmpty()
		assertThat(result.keyboard).isEqualTo(Keyboard.MINI_APP_LINK)
	}

	@Test
	fun `handle should always include MINI_APP_LINK keyboard`() {
		// Given
		val message = mock<Message>()

		// When
		val result = command.handle(message)

		// Then
		assertThat(result.keyboard).isNotNull
		assertThat(result.keyboard).isEqualTo(Keyboard.MINI_APP_LINK)
	}

	@Test
	fun `handle should work with different messages`() {
		// Given
		val message1 = mock<Message>()
		val message2 = mock<Message>()

		// When
		val result1 = command.handle(message1)
		val result2 = command.handle(message2)

		// Then - results should be consistent
		assertThat(result1.localizationCode).isEqualTo(result2.localizationCode)
		assertThat(result1.params).isEqualTo(result2.params)
		assertThat(result1.keyboard).isEqualTo(result2.keyboard)
	}
}