package com.soloist.telegram.bot.state.impl

import com.soloist.proto.common.ProofType
import com.soloist.telegram.bot.client.TaskClient
import com.soloist.telegram.bot.service.message.TelegramMessageFactory
import com.soloist.telegram.bot.service.message.TelegramMessageSender
import com.soloist.telegram.bot.state.StateProcessor
import com.soloist.telegram.localization.InfoCode
import com.soloist.telegram.model.entity.user.state.proof.ProofSubmissionState
import io.grpc.StatusRuntimeException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.reflect.KClass

@Component
class ProofSubmissionStateProcessor(
	private val taskClient: TaskClient,
	private val telegramMessageFactory: TelegramMessageFactory,
	private val telegramMessageSender: TelegramMessageSender
) : StateProcessor<ProofSubmissionState> {

	private val log = LoggerFactory.getLogger(javaClass)

	override fun getStateClass(): KClass<ProofSubmissionState> = ProofSubmissionState::class

	override fun process(message: Message, state: ProofSubmissionState): Boolean {
		val expectedType = state.proofType()
		val (proofType, text, fileId) = extractProof(message, expectedType) ?: return false

		val response = try {
			taskClient.submitTaskProof(
				taskId = state.taskId(),
				proofType = proofType,
				text = text,
				telegramFileId = fileId,
				secretWord = state.secretWord().orEmpty()
			)
		} catch (e: StatusRuntimeException) {
			log.error("gRPC error submitting proof for taskId={} userId={}", state.taskId(), message.chatId, e)
			telegramMessageSender.send(
				telegramMessageFactory.sendMessage(message.chatId, InfoCode.PROOF_ERROR)
			)
			return true
		}

		val resultMessage = if (response.isApproved) {
			val isCompleted = response.gemReward > 0
			if (isCompleted) {
				telegramMessageFactory.sendMessage(
					message.chatId,
					InfoCode.PROOF_COMPLETED,
					params = listOf(response.progressIncrement, response.progress, response.goal, response.gemReward)
				)
			} else {
				telegramMessageFactory.sendMessage(
					message.chatId,
					InfoCode.PROOF_APPROVED,
					params = listOf(response.progressIncrement, response.progress, response.goal)
				)
			}
		} else {
			telegramMessageFactory.sendMessage(
				message.chatId,
				InfoCode.PROOF_REJECTED,
				params = listOf(response.rejectionReason)
			)
		}

		telegramMessageSender.send(resultMessage)
		return true
	}

	private data class ProofData(val proofType: ProofType, val text: String, val fileId: String)

	private fun extractProof(message: Message, expectedType: String): ProofData? {
		return when (expectedType) {
			"TEXT" -> {
				if (!message.hasText()) return null
				ProofData(ProofType.TEXT, message.text, "")
			}
			"PHOTO" -> {
				if (!message.hasPhoto()) return null
				val fileId = message.photo
					.maxByOrNull { it.fileSize ?: 0 }
					?.fileId
					?: return null
				ProofData(ProofType.PHOTO, message.caption.orEmpty(), fileId)
			}
			"VIDEO" -> {
				val fileId = when {
					message.hasVideo() -> message.video.fileId
					message.hasVideoNote() -> message.videoNote.fileId
					else -> return null
				}
				ProofData(ProofType.VIDEO, message.caption.orEmpty(), fileId)
			}
			else -> null
		}
	}
}
