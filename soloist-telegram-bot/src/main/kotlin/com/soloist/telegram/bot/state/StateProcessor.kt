package com.soloist.telegram.bot.state

import com.soloist.telegram.model.entity.user.state.BotSessionState
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.reflect.KClass

interface StateProcessor<T : BotSessionState> {

	/**
	 * Возвращает класс состояния, которое обрабатывает этот процессор
	 */
	fun getStateClass(): KClass<T>

	/**
	 * Выполняет бизнес-логику для состояния
	 */
	fun process(message: Message, state: T): Boolean
}
