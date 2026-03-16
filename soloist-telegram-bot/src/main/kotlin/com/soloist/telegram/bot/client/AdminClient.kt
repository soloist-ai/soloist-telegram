package com.soloist.telegram.bot.client

import com.google.protobuf.Empty
import com.soloist.proto.admin.AdminServiceGrpc.AdminServiceBlockingStub
import com.soloist.proto.admin.DeprecateTasksByTopicRequest
import com.soloist.proto.admin.GetUsersRequest
import com.soloist.proto.admin.GetUsersResponse
import com.soloist.proto.admin.GetUsersStatsResponse
import com.soloist.proto.admin.ResetPlayerRequest
import com.soloist.proto.common.RequestPaging
import com.soloist.proto.common.TaskTopic
import org.springframework.stereotype.Service

@Service
class AdminClient(
	private val adminStub: AdminServiceBlockingStub
) {

	fun resetPlayer(id: Long) {
		adminStub.resetPlayer(ResetPlayerRequest.newBuilder().setPlayerId(id).build())
	}

	fun deprecateAllTasks(): Int =
		adminStub.deprecateAllTasks(Empty.newBuilder().build()).affectedRows

	fun deprecateTasksByTopic(taskTopic: TaskTopic): Int =
		adminStub.deprecateTasksByTopic(
			DeprecateTasksByTopicRequest.newBuilder().setTaskTopic(taskTopic).build()
		).affectedRows

	fun getUsersStats(): GetUsersStatsResponse =
		adminStub.getUsersStats(Empty.newBuilder().build())

	fun getUsers(page: Int, pageSize: Int): GetUsersResponse =
		adminStub.getUsers(
			GetUsersRequest.newBuilder()
				.setPaging(RequestPaging.newBuilder().setPage(page).setPageSize(pageSize))
				.build()
		)
}
