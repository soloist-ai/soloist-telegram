package com.soloist.telegram.bot.state.impl

import com.soloist.telegram.bot.service.user.UserFeedbackService
import com.soloist.telegram.bot.state.StateProcessor
import com.soloist.telegram.model.entity.user.state.feedback.FeedbackMessageState
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.reflect.KClass

@Component
class FeedbackMessageStateProcessor(
	private val userFeedbackService: UserFeedbackService
) : StateProcessor<FeedbackMessageState> {

	override fun getStateClass(): KClass<FeedbackMessageState> = FeedbackMessageState::class

	override fun process(message: Message, state: FeedbackMessageState): Boolean {
		if (!message.hasText()) {
			return false
		}

		userFeedbackService.create(message.chatId, message.text)
		return true
	}
}