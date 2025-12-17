package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.feedback.FeedbackMessageState
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class FeedbackCommand(
	override val userSessionService: UserSessionService
) : InterruptCommand<FeedbackMessageState> {

	override val command: String = "/feedback"
	override val forList: Boolean = true
	override val description: String = "Send feedback"

	override fun createState(message: Message, session: UserSession): FeedbackMessageState =
		FeedbackMessageState()
}
