package com.sleepkqq.sololeveling.telegram.bot.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CommandTest {

	private class TestCommand : Command {
		override val command: String = "/test"
		override val visible: Boolean = true
		override val description: String = "Test description"
	}

	private class TestCommandWithDefaultDescription : Command {
		override val command: String = "/test2"
		override val visible: Boolean = false
		// Using default description
	}

	@Test
	fun `Command interface should have command property`() {
		// Given
		val testCommand = TestCommand()

		// When/Then
		assertThat(testCommand.command).isEqualTo("/test")
	}

	@Test
	fun `Command interface should have visible property`() {
		// Given
		val testCommand = TestCommand()

		// When/Then
		assertThat(testCommand.visible).isTrue()
	}

	@Test
	fun `Command interface should have description property`() {
		// Given
		val testCommand = TestCommand()

		// When/Then
		assertThat(testCommand.description).isEqualTo("Test description")
	}

	@Test
	fun `Command interface should have default empty description`() {
		// Given
		val testCommand = TestCommandWithDefaultDescription()

		// When/Then
		assertThat(testCommand.description).isEmpty()
	}

	@Test
	fun `Command interface should allow different implementations`() {
		// Given
		val command1 = TestCommand()
		val command2 = TestCommandWithDefaultDescription()

		// When/Then
		assertThat(command1.command).isNotEqualTo(command2.command)
		assertThat(command1.visible).isNotEqualTo(command2.visible)
	}

	@Test
	fun `Command visible can be false`() {
		// Given
		val testCommand = TestCommandWithDefaultDescription()

		// When/Then
		assertThat(testCommand.visible).isFalse()
	}
}