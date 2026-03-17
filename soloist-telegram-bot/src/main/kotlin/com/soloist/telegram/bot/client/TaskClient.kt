package com.soloist.telegram.bot.client

import com.soloist.proto.common.ProofType
import com.soloist.proto.task.SubmitTaskProofRequest
import com.soloist.proto.task.SubmitTaskProofResponse
import com.soloist.proto.task.TaskServiceGrpc.TaskServiceBlockingStub
import org.springframework.stereotype.Service

@Service
class TaskClient(
	private val taskStub: TaskServiceBlockingStub
) {

	fun submitTaskProof(
		taskId: String,
		proofType: ProofType,
		text: String = "",
		telegramFileId: String = "",
		secretWord: String = ""
	): SubmitTaskProofResponse =
		taskStub.submitTaskProof(
			SubmitTaskProofRequest.newBuilder()
				.setTaskId(taskId)
				.setProofType(proofType)
				.setText(text)
				.setTelegramFileId(telegramFileId)
				.setSecretWord(secretWord)
				.build()
		)
}
