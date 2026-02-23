package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCommand
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.localization.CommandDescriptionCode
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter.NewsletterNameState
import org.telegram.telegrambots.meta.api.objects.message.Message

@TelegramCommand("send_newsletter", CommandDescriptionCode.SEND_NEWSLETTER)
class SendNewsletterCommand(
	override val userSessionService: UserSessionService
) : InterruptCommand {

	override fun createState(message: Message, session: UserSession): BotSessionState? =
		NewsletterNameState()
}
