package com.soloist.telegram.bot.command.interrupt

import com.soloist.telegram.bot.annotation.TelegramCommand
import com.soloist.telegram.bot.service.user.UserSessionService
import com.soloist.telegram.localization.CommandDescriptionCode
import com.soloist.telegram.model.entity.user.UserSession
import com.soloist.telegram.model.entity.user.state.BotSessionState
import com.soloist.telegram.model.entity.user.state.newsletter.NewsletterNameState
import org.telegram.telegrambots.meta.api.objects.message.Message

@TelegramCommand("send_newsletter", CommandDescriptionCode.SEND_NEWSLETTER)
class SendNewsletterCommand(
	override val userSessionService: UserSessionService
) : InterruptCommand {

	override fun createState(message: Message, session: UserSession): BotSessionState? =
		NewsletterNameState()
}
