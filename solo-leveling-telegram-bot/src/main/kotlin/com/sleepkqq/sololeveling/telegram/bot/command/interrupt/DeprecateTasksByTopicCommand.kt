package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.task.DeprecateTasksByTopicState
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class DeprecateTasksByTopicCommand(
	override val userSessionService: UserSessionService
) : InterruptCommand<DeprecateTasksByTopicState> {

	override val command: String = "/deprecate_tasks_by_topic"

	@PreAuthorize("hasAuthority('DEVELOPER')")
	override fun createState(message: Message, session: UserSession): DeprecateTasksByTopicState =
		DeprecateTasksByTopicState()
}
