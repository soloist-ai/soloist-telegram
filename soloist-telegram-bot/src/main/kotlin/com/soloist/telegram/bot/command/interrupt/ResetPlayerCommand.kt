package com.soloist.telegram.bot.command.interrupt

import com.soloist.config.interceptor.UserContextHolder
import com.soloist.telegram.bot.annotation.TelegramCommand
import com.soloist.telegram.bot.model.UserRole
import com.soloist.telegram.bot.service.auth.AuthService
import com.soloist.telegram.bot.service.user.UserSessionService
import com.soloist.telegram.localization.CommandDescriptionCode
import com.soloist.telegram.model.entity.user.UserSession
import com.soloist.telegram.model.entity.user.state.BotSessionState
import com.soloist.telegram.model.entity.user.state.player.ResetPlayerConfirmationState
import com.soloist.telegram.model.entity.user.state.player.ResetPlayerIdState
import org.telegram.telegrambots.meta.api.objects.message.Message

@TelegramCommand("reset_player", CommandDescriptionCode.RESET_PLAYER)
class ResetPlayerCommand(
	override val userSessionService: UserSessionService,
	private val authService: AuthService
) : InterruptCommand {

	override fun createState(message: Message, session: UserSession): BotSessionState? =
		if (authService.hasRole(UserRole.ADMIN)) {
			ResetPlayerIdState()
		} else {
			ResetPlayerConfirmationState(UserContextHolder.getUserId()!!)
		}
}
