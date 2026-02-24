package com.soloist.telegram.bot.command.interrupt

import com.soloist.telegram.bot.annotation.TelegramCommand
import com.soloist.telegram.bot.service.user.UserSessionService
import com.soloist.telegram.localization.CommandDescriptionCode
import com.soloist.telegram.model.entity.user.UserSession
import com.soloist.telegram.model.entity.user.state.BotSessionState
import com.soloist.telegram.model.entity.user.state.feedback.FeedbackMessageState
import org.telegram.telegrambots.meta.api.objects.message.Message

@TelegramCommand("feedback", CommandDescriptionCode.FEEDBACK)
class FeedbackCommand(
	override val userSessionService: UserSessionService
) : InterruptCommand {

	override fun createState(message: Message, session: UserSession): BotSessionState? =
		FeedbackMessageState()
}
