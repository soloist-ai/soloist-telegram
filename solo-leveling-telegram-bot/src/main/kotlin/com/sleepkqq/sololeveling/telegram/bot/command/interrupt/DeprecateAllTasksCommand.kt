package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.task.DeprecateAllTasksConfirmationState
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class DeprecateAllTasksCommand(
	override val userSessionService: UserSessionService
) : InterruptCommand<DeprecateAllTasksConfirmationState> {

	override val command: String = "/deprecate_all_tasks"

	@PreAuthorize("hasAuthority('DEVELOPER')")
	override fun createState(
		message: Message,
		session: UserSession
	): DeprecateAllTasksConfirmationState = DeprecateAllTasksConfirmationState()
}
