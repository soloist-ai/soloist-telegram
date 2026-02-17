package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.command.interrupt.InterruptCommand.InterruptCommandResult
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter.NewsletterMessageState
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class SendNewsletterCommand(
	override val userSessionService: UserSessionService
) : InterruptCommand<NewsletterMessageState> {

	override val command: String = "/send-newsletter"
	override val visible: Boolean = false

	@PreAuthorize("hasAuthority('ADMIN')")
	override fun handle(
		message: Message,
		session: UserSession
	): InterruptCommandResult<NewsletterMessageState>? = super.handle(message, session)

	override fun createState(message: Message, session: UserSession): NewsletterMessageState =
		NewsletterMessageState()
}
