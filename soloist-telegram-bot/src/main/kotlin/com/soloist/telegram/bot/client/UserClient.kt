package com.soloist.telegram.bot.client

import com.google.protobuf.Empty
import com.soloist.proto.common.RequestPaging
import com.soloist.proto.user.GetUserRequest
import com.soloist.proto.user.GetUsersRequest
import com.soloist.proto.user.GetUsersResponse
import com.soloist.proto.user.GetUsersStatsResponse
import com.soloist.proto.user.UserServiceGrpc.UserServiceBlockingStub
import com.soloist.proto.user.UserView
import org.springframework.stereotype.Service

@Service
class UserClient(
	private val userStub: UserServiceBlockingStub
) {

	fun getUser(id: Long): UserView =
		userStub.getUser(GetUserRequest.newBuilder().setUserId(id).build()).user

	fun getUsersStats(): GetUsersStatsResponse =
		userStub.getUsersStats(Empty.newBuilder().build())

	fun getUsers(page: Int, pageSize: Int): GetUsersResponse =
		userStub.getUsers(
			GetUsersRequest.newBuilder()
				.setPaging(RequestPaging.newBuilder().setPage(page).setPageSize(pageSize))
				.build()
		)
}
