package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.config.interceptor.UserContextHolder
import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCommand
import com.sleepkqq.sololeveling.telegram.bot.model.UserRole
import com.sleepkqq.sololeveling.telegram.bot.service.auth.AuthService
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.localization.CommandDescriptionCode
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.player.ResetPlayerConfirmationState
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.player.ResetPlayerIdState
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
