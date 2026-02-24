package com.soloist.telegram.bot.grpc.client

import com.google.protobuf.Empty
import com.soloist.proto.player.DeprecateTasksByTopicRequest
import com.soloist.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub
import com.soloist.proto.player.ResetPlayerRequest
import com.soloist.proto.player.TaskTopic
import org.springframework.stereotype.Service

@Service
class PlayerApi(
	private val playerStub: PlayerServiceBlockingStub
) {

	fun deprecateAllTasks(): Int =
		playerStub.deprecateAllTasks(Empty.newBuilder().build()).affectedRows

	fun deprecateTasksByTopic(taskTopic: TaskTopic): Int = playerStub.deprecateTasksByTopic(
		DeprecateTasksByTopicRequest.newBuilder().setTaskTopic(taskTopic).build()
	).affectedRows

	fun resetPlayer(id: Long) {
		playerStub.resetPlayer(ResetPlayerRequest.newBuilder().setPlayerId(id).build())
	}
}