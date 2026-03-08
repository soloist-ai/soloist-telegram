package com.soloist.telegram.bot.client

import com.google.protobuf.Empty
import com.soloist.proto.common.TaskTopic
import com.soloist.proto.task.DeprecateTasksByTopicRequest
import com.soloist.proto.task.TaskServiceGrpc
import org.springframework.stereotype.Service

@Service
class TaskClient(
	private val taskStub: TaskServiceGrpc.TaskServiceBlockingStub
) {

	fun deprecateAllTasks(): Int =
		taskStub.deprecateAllTasks(Empty.newBuilder().build()).affectedRows

	fun deprecateTasksByTopic(taskTopic: TaskTopic): Int = taskStub.deprecateTasksByTopic(
		DeprecateTasksByTopicRequest.newBuilder().setTaskTopic(taskTopic).build()
	).affectedRows
}
