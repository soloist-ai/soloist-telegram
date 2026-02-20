package com.sleepkqq.sololeveling.telegram.bot.command.info

import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.telegram.telegrambots.meta.api.objects.message.Message

class HelpCommandTest {

	private lateinit var command: HelpCommand

	@BeforeEach
	fun setUp() {
		command = HelpCommand()
	}

	@Test
	fun `command should return help command string`() {
		// When/Then
		assertThat(command.command).isEqualTo("/help")
	}

	@Test
	fun `visible should return true`() {
		// When/Then
		assertThat(command.visible).isTrue()
	}

	@Test
	fun `description should return help description`() {
		// When/Then
		assertThat(command.description).isEqualTo("Show help")
	}

	@Test
	fun `handle should return InfoCommandResult with CMD_HELP localization code`() {
		// Given
		val message = mock<Message>()

		// When
		val result = command.handle(message)

		// Then
		assertThat(result).isNotNull
		assertThat(result.localizationCode).isEqualTo(LocalizationCode.CMD_HELP)
		assertThat(result.params).isEmpty()
		assertThat(result.keyboard).isNull()
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