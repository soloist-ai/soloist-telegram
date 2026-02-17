package com.sleepkqq.sololeveling.telegram.bot.command.interrupt

import com.sleepkqq.sololeveling.telegram.bot.command.interrupt.InterruptCommand.InterruptCommandResult
import com.sleepkqq.sololeveling.telegram.bot.grpc.client.UserApi
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.player.ResetPlayerConfirmationState
import io.grpc.StatusRuntimeException
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class ResetPlayerCommand(
	override val userSessionService: UserSessionService,
	private val userApi: UserApi
) : InterruptCommand<ResetPlayerConfirmationState> {

	override val command: String = "/reset_player"
	override val visible: Boolean = false

	private val log = LoggerFactory.getLogger(javaClass)

	@PreAuthorize("hasAuthority('ADMIN')")
	override fun handle(
		message: Message,
		session: UserSession
	): InterruptCommandResult<ResetPlayerConfirmationState>? = super.handle(message, session)

	override fun createState(
		message: Message,
		session: UserSession
	): ResetPlayerConfirmationState? {

		val parts = message.text?.trim()?.split(Regex("\\s+")) ?: return null

		if (parts.size != 2) return null

		val userId = parts[1].toLongOrNull()?.takeIf { it > 0 } ?: return null

		return try {
			val user = userApi.getUser(userId).user

			ResetPlayerConfirmationState(userId, user.username, user.firstName, user.lastName)

		} catch (ex: StatusRuntimeException) {
			log.error("Failed to get user $userId", ex)
			null
		}
	}
}
