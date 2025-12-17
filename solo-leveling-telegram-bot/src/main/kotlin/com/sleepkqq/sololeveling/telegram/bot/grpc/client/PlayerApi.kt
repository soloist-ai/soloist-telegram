package com.sleepkqq.sololeveling.telegram.bot.grpc.client

import com.google.protobuf.Empty
import com.sleepkqq.sololeveling.proto.player.DeprecateTasksByTopicRequest
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub
import com.sleepkqq.sololeveling.proto.player.ResetPlayerRequest
import com.sleepkqq.sololeveling.proto.player.TaskTopic
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