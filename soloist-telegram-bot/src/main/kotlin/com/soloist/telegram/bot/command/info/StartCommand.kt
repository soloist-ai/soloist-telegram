package com.soloist.telegram.bot.command.info

import com.soloist.telegram.bot.annotation.TelegramCommand
import com.soloist.telegram.bot.command.interrupt.InterruptCommand
import com.soloist.telegram.bot.service.SecretWordGenerator
import com.soloist.telegram.bot.service.user.UserSessionService
import com.soloist.telegram.keyboard.Keyboard
import com.soloist.telegram.localization.CommandCode
import com.soloist.telegram.localization.CommandDescriptionCode
import com.soloist.telegram.localization.Localized
import com.soloist.telegram.model.entity.user.UserSession
import com.soloist.telegram.model.entity.user.state.BotSessionState
import com.soloist.telegram.model.entity.user.state.proof.ProofSubmissionState
import org.telegram.telegrambots.meta.api.objects.message.Message

@TelegramCommand("start", CommandDescriptionCode.START)
class StartCommand(
	override val userSessionService: UserSessionService,
	private val secretWordGenerator: SecretWordGenerator
) : InterruptCommand {

	override fun createState(message: Message, session: UserSession): BotSessionState? {
		val params = extractProofParams(message) ?: return null
		val secretWord = if (params.proofType == "VIDEO") secretWordGenerator.generate() else null
		return ProofSubmissionState(params.taskId, params.proofType, secretWord)
	}

	override fun handle(
		message: Message,
		session: UserSession
	): InterruptCommand.InterruptCommandResult? {
		extractProofParams(message) ?: return InterruptCommand.InterruptCommandResult.Info(
			localized = object : Localized {
				override val localizationCode = CommandCode.START
				override val keyboard = Keyboard.MINI_APP_LINK
			}
		)
		return super.handle(message, session)
	}

	private data class ProofParams(val taskId: String, val proofType: String)

	private fun extractProofParams(message: Message): ProofParams? {
		val text = message.text ?: return null
		val parts = text.trim().split(" ")
		if (parts.size < 2) return null
		val param = parts[1]
		if (!param.startsWith(PROOF_PREFIX)) return null

		val payload = param.removePrefix(PROOF_PREFIX)
		val segments = payload.split("_", limit = 2)
		val taskId = segments.getOrNull(0)?.ifBlank { null } ?: return null
		val proofType = segments.getOrNull(1)?.uppercase() ?: "TEXT"

		return ProofParams(taskId, proofType)
	}

	private companion object {
		const val PROOF_PREFIX = "proof_"
	}
}
